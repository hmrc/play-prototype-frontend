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
import uk.gov.hmrc.playprototypefrontend.model.{PersonalDetails, addressForm, contactForm, nameForm, personalDetailsForm, personalDetailsFormat, phoneForm}
import uk.gov.hmrc.playprototypefrontend.views.html._

import scala.concurrent.Future
import scala.language.implicitConversions

@Singleton
class PersonalDetailsAccountController @Inject()(
                                                  personalDetailsStart: PersonalDetailsAccount,
                                                  nameView: Name,
                                                  phoneView: Phone,
                                                  addressView: Address,
                                                  contactView: ContactPreference,
                                                  summaryView: Summary,
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
          case Some(pda) => pda.copy(phone = personalDetails.phone)
          case _ => personalDetails
        }
        Future.successful(Ok(addressView(phoneForm)).addingToSession("personalDetails" -> Json.toJson(result).toString()))
      }
    )
  }

  val addressPage: Action[AnyContent] = Action.async { implicit request =>

    Future.successful(Ok(addressView(personalDetailsForm)))
  }

  val acceptAddress: Action[AnyContent] = Action.async { implicit request =>

    addressForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(addressView(formWithErrors)))
      },
      personalDetails => {
        val storedPersonalDetails = request.session.get("personalDetails").map(Json.parse).map(Json.fromJson[PersonalDetails]).map(_.get)
        val result = storedPersonalDetails match {
          case Some(pda) => pda.copy(address = personalDetails.address)
          case _ => personalDetails
        }
        Future.successful(Ok(contactView(addressForm)).addingToSession("personalDetails" -> Json.toJson(result).toString()))
      }
    )
  }

  val contactPage: Action[AnyContent] = Action.async { implicit request =>

    Future.successful(Ok(contactView(personalDetailsForm)))
  }

  val acceptContact: Action[AnyContent] = Action.async { implicit request =>

    contactForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(contactView(formWithErrors)))
      },
      personalDetails => {
        val storedPersonalDetails = request.session.get("personalDetails").map(Json.parse).map(Json.fromJson[PersonalDetails]).map(_.get)
        val result = storedPersonalDetails match {
          case Some(pda) => pda.copy(contact = personalDetails.contact)
          case _ => personalDetails
        }
        Future.successful(Ok(summaryView(contactForm)).addingToSession("personalDetails" -> Json.toJson(result).toString()))
      }
    )
  }

}
