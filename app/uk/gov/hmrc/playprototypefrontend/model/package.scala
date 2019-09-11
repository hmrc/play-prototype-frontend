/*
 * Copyright 2019 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.playprototypefrontend

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.json.Json

package object model {

  case class Name(name: String)

  case class PhoneNumber(phoneNumber: String)

  case class PersonalDetails(name: Name = Name(""),
                             phone: PhoneNumber = PhoneNumber(""),
                             csrfToken: String = "")

  implicit val optionStringFormat = play.api.libs.json.Format.optionWithNull[String]

  implicit val nameFormat = Json.format[Name]

  implicit val phoneFormat = Json.format[PhoneNumber]

  implicit val personalDetailsFormat = Json.format[PersonalDetails]

  val nameForm = Form[PersonalDetails](
    mapping(
      "name" -> nonEmptyText.verifying(pattern(regex = """\D+""".r, error = "name.inputInvalid", name = "")),
      "csrfToken" -> nonEmptyText
    )(
      (a, b) => PersonalDetails(name = Name(a), csrfToken = b)
    )(
      personalDetails =>
        Some(personalDetails.name.name, personalDetails.csrfToken)
    )
  )

  val phoneForm = Form[PersonalDetails](
    mapping(
      "phone-number" -> nonEmptyText.verifying(pattern(regex = """\d+""".r, error = "phone.inputInvalid", name = "")),
      "csrfToken" -> nonEmptyText
    )(
      (a, b) => PersonalDetails(name = Name(a), csrfToken = b)
    )(
      personalDetails =>
        Some(personalDetails.name.name, personalDetails.csrfToken)
    )
  )

  val personalDetailsForm = Form[PersonalDetails](
    mapping(
      "name" -> text.verifying(pattern(regex = """\D+""".r, error = "name.inputInvalid", name = "")),
      "phone-number" -> nonEmptyText.verifying(pattern(regex = """\d+""".r, error = "phone.inputInvalid", name = "")),
      "csrfToken" -> nonEmptyText
    )(
      (a, b, c) => PersonalDetails(Name(a), PhoneNumber(b), c)
    )(
      personalDetails =>
        Some(personalDetails.name.name, personalDetails.phone.phoneNumber, personalDetails.csrfToken)
    )
  )

}