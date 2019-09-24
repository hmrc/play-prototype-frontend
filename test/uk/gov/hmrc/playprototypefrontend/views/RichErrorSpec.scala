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

package uk.gov.hmrc.playprototypefrontend.views

import org.scalatest.{Matchers, WordSpec}
import play.api.data._
import play.api.i18n.{DefaultMessagesApi, Messages}
import uk.gov.hmrc.govukfrontend.views.viewmodels.common.{HtmlContent, Text}
import uk.gov.hmrc.govukfrontend.views.viewmodels.errormessage.ErrorMessageParams
import uk.gov.hmrc.govukfrontend.views.viewmodels.errorsummary.ErrorLink

class RichErrorSpec extends WordSpec with Matchers {

  val messagesApi = new DefaultMessagesApi(
    messages = Map("default" -> Map("error.invalid" -> "Invalid input received", "error.missing" -> "Input missing")))

  implicit val messages: Messages = messagesApi.preferred(Seq.empty)

  "Form errors" should {

    val errors =
      Seq(FormError("field1", "error.invalid"), FormError("field2", "error.missing"))

    "be transformed to error links" in {

      errors.asErrorLinks should contain theSameElementsAs (
        Seq(
          ErrorLink(href = Some("#field1"), content  = HtmlContent("Invalid input received")),
          ErrorLink(href = Some(s"#field2"), content = HtmlContent("Input missing"))
        )
      )
    }

    "be transformed to error messages" in {

      errors.asErrorMessage should contain theSameElementsAs (
        Seq(
          ErrorMessageParams(content = Text("Invalid input received")),
          ErrorMessageParams(content = Text("Input missing"))
        )
      )
    }

    "be transformed to error messages matching selection criteria" in {

      errors.asErrorMessage("error.missing").get shouldBe ErrorMessageParams(content = Text("Input missing"))
    }
  }
}