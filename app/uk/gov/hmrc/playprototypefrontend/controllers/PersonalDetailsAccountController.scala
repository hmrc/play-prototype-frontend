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
                                                  personalDetailsConfirm: PersonalDetailsAccountConfirmation,
                                                  clearView: Clear,
                                                  clearSuccessView: ClearSuccess,
                                                  appConfig: AppConfig,
                                                  mcc: MessagesControllerComponents
                                                ) extends FrontendController(mcc) {

  implicit val config: AppConfig = appConfig

  val startPage: Action[AnyContent] = Action.async { implicit request =>

    Future.successful(Ok(personalDetailsStart()))
  }

  val namePage: Action[AnyContent] = Action.async { implicit request =>

    Future.successful(Ok(nameView(personalDetailsForm.fill(personalDetailsInSession))))
  }

  val acceptName: Action[AnyContent] = Action.async { implicit request =>

    nameForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(nameView(formWithErrors)))
      },
      personalDetails => {
        val result = personalDetailsInSession.copy(name = personalDetails.name)
        Future.successful(Ok(phoneView(nameForm.fill(result))).withSession(updatingPersonalDetails(result)))
      }
    )
  }

  val phonePage: Action[AnyContent] = Action.async { implicit request =>

    Future.successful(Ok(phoneView(personalDetailsForm.fill(personalDetailsInSession))))
  }

  val acceptPhone: Action[AnyContent] = Action.async { implicit request =>

    phoneForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(phoneView(formWithErrors)))
      },
      personalDetails => {
        val result = personalDetailsInSession.copy(phone = personalDetails.phone)
        Future.successful(Ok(addressView(phoneForm.fill(result))).withSession(updatingPersonalDetails(result)))
      }
    )
  }

  val addressPage: Action[AnyContent] = Action.async { implicit request =>

    Future.successful(Ok(addressView(personalDetailsForm.fill(personalDetailsInSession))))
  }

  val acceptAddress: Action[AnyContent] = Action.async { implicit request =>

    addressForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(addressView(formWithErrors)))
      },
      personalDetails => {
        val result = personalDetailsInSession.copy(address = personalDetails.address)
        Future.successful(Ok(contactView(addressForm.fill(result))).withSession(updatingPersonalDetails(result)))
      }
    )
  }

  val contactPage: Action[AnyContent] = Action.async { implicit request =>

    Future.successful(Ok(contactView(personalDetailsForm.fill(personalDetailsInSession))))
  }

  val acceptContact: Action[AnyContent] = Action.async { implicit request =>

    contactForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(contactView(formWithErrors)))
      },
      personalDetails => {
        val result = personalDetailsInSession.copy(canWeWrite = personalDetails.canWeWrite)
        Future.successful(Ok(summaryView(contactForm.fill(result))).withSession(updatingPersonalDetails(result)))
      }
    )
  }

  val confirmPage: Action[AnyContent] = Action.async { implicit request =>

    Future.successful(Ok(personalDetailsConfirm()))
  }

  val clearPage: Action[AnyContent] = Action.async { implicit request =>

    Future.successful(Ok(clearView()))
  }

  val clearSuccessPage: Action[AnyContent] = Action.async { implicit request =>

    Future.successful(Ok(clearSuccessView()).withNewSession)
  }

  private def personalDetailsInSession(implicit request: MessagesRequest[_]) = {
    request.session.get("personalDetails").map(Json.parse).map(Json.fromJson[PersonalDetails]).map(_.get) getOrElse PersonalDetails()
  }

  private def updatingPersonalDetails(result: PersonalDetails)(implicit request: MessagesRequest[_]) = {
    request.session + ("personalDetails" -> Json.toJson(result).toString())
  }
}