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

import javax.inject.{Inject, Singleton}
import play.api.libs.json._
import play.api.mvc._
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import uk.gov.hmrc.playprototypefrontend.config.AppConfig
import uk.gov.hmrc.playprototypefrontend.model.{PersonalDetails, nameForm, personalDetailsForm, personalDetailsFormat, phoneForm}
import uk.gov.hmrc.playprototypefrontend.views.html._

import scala.concurrent.Future
import scala.language.implicitConversions

@Singleton
class PersonalDetailsAccountController @Inject()(
                                                  personalDetailsStart: PersonalDetailsAccount,
                                                  nameView: Name,
                                                  phoneView: Phone,
                                                  appConfig: AppConfig,
                                                  mcc: MessagesControllerComponents
                                                ) extends FrontendController(mcc) {

  implicit val config: AppConfig = appConfig

  val startPage: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(personalDetailsStart()))
  }

  val namePage: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(nameView(personalDetailsForm)))
  }

  val acceptName: Action[AnyContent] = Action.async { implicit request =>
    nameForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(nameView(formWithErrors)))
      },
      personalDetails => {
        Future.successful(Ok(phoneView(nameForm)).addingToSession("personalDetails" -> Json.toJson(personalDetails).toString()))
      }
    )
  }

  val phonePage: Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(phoneView(personalDetailsForm)))
  }

  val acceptPhone: Action[AnyContent] = Action.async { implicit request =>
    phoneForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(phoneView(formWithErrors)))
      },
      personalDetails => {
        val storedPersonalDetails = request.session.get("personalDetails").map(Json.parse).map(Json.fromJson[PersonalDetails]).map(_.get)
        val result = storedPersonalDetails match {
          case Some(pda) => pda.copy(name = pda.name, phone = personalDetails.phone)
          case _ => personalDetails
        }
        Future.successful(Ok(personalDetailsStart()))
      }
    )
  }

}
