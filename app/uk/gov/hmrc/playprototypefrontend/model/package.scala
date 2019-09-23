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

import scala.collection.immutable.Stream.Empty

package object model {

  case class Name(name: String)

  case class PhoneNumber(phoneNumber: String)

  case class Address(lines: Seq[String] = Empty) {

    def asText = lines mkString ("\r\n")
  }

  case class PersonalDetails(name: Name = Name(""),
                             phone: PhoneNumber = PhoneNumber(""),
                             address: Address = Address(),
                             canWeWrite: Option[String] = None)

  implicit val optionStringFormat = play.api.libs.json.Format.optionWithNull[String]

  implicit val nameFormat = Json.format[Name]

  implicit val phoneFormat = Json.format[PhoneNumber]

  implicit val addressFormat = Json.format[Address]

  implicit val personalDetailsFormat = Json.format[PersonalDetails]

  val nameForm = Form[PersonalDetails](
    mapping(
      "name" -> text.verifying(pattern(regex = """\D+""".r, error = "name.inputInvalid", name = ""))
    )(
      a => PersonalDetails(name = Name(a))
    )(
      personalDetails =>
        Some(personalDetails.name.name)
    )
  )

  val phoneForm = Form[PersonalDetails](
    mapping(
      "phone-number" -> text.verifying(pattern(regex = """07\d{9}""".r, error = "phone.inputInvalid", name = ""))
    )(
      a => PersonalDetails(phone = PhoneNumber(a))
    )(
      personalDetails =>
        Some(personalDetails.phone.phoneNumber)
    )
  )

  val addressForm = Form[PersonalDetails](
    mapping(
      "address" -> text.verifying(pattern(regex = """\w(\w|,|\s)*""".r, error = "address.inputInvalid", name = ""))
    )(
      a => PersonalDetails(address = Address(a.split("\r\n")))
    )(
      personalDetails =>
        Some(personalDetails.address.asText)
    )
  )

  val contactForm = Form[PersonalDetails](
    mapping(
      "canWeWrite" -> optional(text).verifying("contact.inputInvalid",
        canWeWrite => canWeWrite match {
          case None => false
          case Some(_) => true
        }
      )
    )(
      a => PersonalDetails(canWeWrite = a)
    )(
      personalDetails => Some(personalDetails.canWeWrite)
    )
  )

  val personalDetailsForm = Form[PersonalDetails](
    mapping(
      "name" -> text.verifying(pattern(regex = """\D+""".r, error = "name.inputInvalid", name = "")),
      "phone-number" -> text.verifying(pattern(regex = """\d+""".r, error = "phone.inputInvalid", name = "")),
      "address" -> text.verifying(pattern(regex = """(\w|\s)+""".r, error = "address.inputInvalid", name = "")),
      "canWeWrite" -> optional(text)
    )(
      (a, b, c, d) => PersonalDetails(Name(a), PhoneNumber(b), Address(c.split("\r\n")), d)
    )(
      personalDetails =>
        Some((personalDetails.name.name, personalDetails.phone.phoneNumber, personalDetails.address.asText, personalDetails.canWeWrite))
    )
  )

}