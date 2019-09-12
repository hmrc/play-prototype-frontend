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

import play.api.data.{Field, Form, FormError}
import play.api.i18n.Messages
import play.api.libs.json.Json
import uk.gov.hmrc.govukfrontend.views.html.components._
import uk.gov.hmrc.playprototypefrontend.controllers.routes._
import uk.gov.hmrc.playprototypefrontend.model.PersonalDetails

object ViewUtils {

  def errorPrefix(form: Form[_])(implicit messages: Messages): String = {
    if (form.hasErrors || form.hasGlobalErrors)
      messages("error.browser.title.prefix")
    else ""
  }

  def mapErrors(errors: Seq[FormError])(implicit messages: Messages): Seq[ErrorLink] = {
    errors.map { error =>
      ErrorLink(href = Some(error.key),
        content = HtmlContent(messages(error.message, error.args: _*)))
    }
  }

  def mapRadioItems(options: Seq[String], field: Field)(implicit messages: Messages): Seq[RadioItem] = {
    options.map { option =>
      RadioItem(content = Text(messages(option)), id = Some(option), value = Some(option), checked = field.value == Some(option))
    }
  }

  def mapName(pda: PersonalDetails): Row =
    Row(key = Key(Text("Name")),
      value = Value(Text(s"${pda.name.name}")),
      actions = Some(Actions(items = Seq(ActionItem(href = s"${PersonalDetailsAccountController.namePage()}", content = Text("Change"))))))

  def mapPhone(pda: PersonalDetails): Row =
    Row(key = Key(Text("Phone number")),
      value = Value(Text(s"${pda.phone.phoneNumber}")),
      actions = Some(Actions(items = Seq(ActionItem(href = s"${PersonalDetailsAccountController.phonePage()}", content = Text("Change"))))))

  def mapAddress(pda: PersonalDetails): Row =
    Row(key = Key(Text("Address")),
      value = Value(Text(s"${pda.address.asText}")),
      actions = Some(Actions(items = Seq(ActionItem(href = s"${PersonalDetailsAccountController.addressPage()}", content = Text("Change"))))))

  def mapContactPref(pda: PersonalDetails): Row =
    Row(key = Key(Text("Can we write to you?")),
      value = Value(Text(s"${pda.contact}")),
      actions = Some(Actions(items = Seq(ActionItem(href = s"${PersonalDetailsAccountController.contactPage()}", content = Text("Change"))))))

  def mapPersonalDetailsToSummary()(implicit messages: Messages, session: play.api.mvc.Session): Seq[Row] = {

    val storedPersonalDetails = session.get("personalDetails").map(Json.parse).map(Json.fromJson[PersonalDetails]).map(_.get)
    val result = storedPersonalDetails match {
      case Some(pda) => pda
      case _ => PersonalDetails()
    }

    Seq(mapName(result), mapPhone(result), mapAddress(result), mapContactPref(result))
  }
}