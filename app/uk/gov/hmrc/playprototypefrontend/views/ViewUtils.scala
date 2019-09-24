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

import play.api.data.{Form, FormError}
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.html.components._
import uk.gov.hmrc.playprototypefrontend.controllers.routes._
import uk.gov.hmrc.playprototypefrontend.model.PersonalDetails

object ViewUtils {

  def errorPrefix(form: Form[_])(implicit messages: Messages): String =
    if (form.hasErrors || form.hasGlobalErrors)
      messages("error.browser.title.prefix")
    else ""

  def mapErrorSummary(errors: Seq[FormError])(implicit messages: Messages): Seq[ErrorLink] =
    errors.map { error =>
      ErrorLink(href = Some(s"#${error.key}"), content = HtmlContent(messages(error.message, error.args: _*)))
    }

  def mapErrorMessage(errors: Seq[FormError], messageSelector: String)(
    implicit messages: Messages): Option[ErrorMessageParams] =
    errors
      .find(_.message == messageSelector)
      .map(error => ErrorMessageParams(content = Text(error.format)))

  def mapRadioItems(options: Seq[String], form: Form[PersonalDetails])(implicit messages: Messages): Seq[RadioItem] =
    options.map { option =>
      RadioItem(
        content = Text(option),
        id      = Some(option),
        value   = Some(option),
        checked = form.value.getOrElse(PersonalDetails()).canWeWrite == Some(option))
    }

  def mapNameToSummary(pda: PersonalDetails): SummaryListRow =
    SummaryListRow(
      key   = Key(Text("Name")),
      value = Value(Text(s"${pda.name.name}")),
      actions = Some(
        Actions(
          items = Seq(ActionItem(href = s"${PersonalDetailsAccountController.namePage()}", content = Text("Change")))))
    )

  def mapPhoneToSummary(pda: PersonalDetails): SummaryListRow =
    SummaryListRow(
      key   = Key(Text("Phone number")),
      value = Value(Text(s"${pda.phone.phoneNumber}")),
      actions = Some(
        Actions(
          items = Seq(ActionItem(href = s"${PersonalDetailsAccountController.phonePage()}", content = Text("Change")))))
    )

  def mapAddressToSummary(pda: PersonalDetails): SummaryListRow =
    SummaryListRow(
      key   = Key(Text("Address")),
      value = Value(Text(s"${pda.address.asText}")),
      actions = Some(Actions(
        items = Seq(ActionItem(href = s"${PersonalDetailsAccountController.addressPage()}", content = Text("Change")))))
    )

  def mapContactPrefToSummary(pda: PersonalDetails): SummaryListRow =
    SummaryListRow(
      key   = Key(Text("Can we write to you?")),
      value = Value(Text(s"${pda.canWeWrite.get}")),
      actions = Some(Actions(
        items = Seq(ActionItem(href = s"${PersonalDetailsAccountController.contactPage()}", content = Text("Change")))))
    )

  def mapPersonalDetailsToSummary(form: Form[PersonalDetails])(implicit messages: Messages): Seq[SummaryListRow] = {
    val details = form.value.getOrElse(PersonalDetails())
    Seq(
      mapNameToSummary(details),
      mapPhoneToSummary(details),
      mapAddressToSummary(details),
      mapContactPrefToSummary(details))
  }
}
