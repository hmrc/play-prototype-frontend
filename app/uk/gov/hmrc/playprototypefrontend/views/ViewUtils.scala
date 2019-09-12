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

import play.api.data.{Form, _}
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.html.components._

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
}