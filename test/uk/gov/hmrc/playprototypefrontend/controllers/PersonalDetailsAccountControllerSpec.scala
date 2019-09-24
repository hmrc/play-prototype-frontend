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

package uk.gov.hmrc.playprototypefrontend.controllers

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.i18n.{Messages, MessagesApi}
import play.api.inject.Injector
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.playprototypefrontend.views.html.PersonalDetailsAccount

class PersonalDetailsAccountControllerSpec extends PlaySpec with GuiceOneAppPerSuite {

  private def applicationBuilder(): GuiceApplicationBuilder = new GuiceApplicationBuilder()
  private def injector: Injector                            = app.injector
  private def messagesApi: MessagesApi                      = injector.instanceOf[MessagesApi]
  private def messages: Messages                            = messagesApi.preferred(fakeRequest)

  private val fakeRequest = FakeRequest("GET", "/")

  "PDA Controller" must {
    "respond to the start Action" in {

      val application = applicationBuilder().build()

      running(application) {
        val request = FakeRequest(GET, routes.PersonalDetailsAccountController.startPage().url)
        val result  = route(application, request).value
        val view    = app.injector.instanceOf[PersonalDetailsAccount]

        status(result) mustBe OK
        contentType(result) mustBe Some("text/html")
        charset(result) mustBe Some("utf-8")
        contentAsString(result) contains view()(fakeRequest, messages).toString
      }
    }
  }
}
