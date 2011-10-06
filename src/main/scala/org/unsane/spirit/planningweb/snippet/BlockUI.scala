package org.unsane.spirit.planningweb.snippet

import _root_.net.liftweb._
import http._
import SHtml._
import js._
import JsCmds._
import js.jquery._
import JqJsCmds._
import common._
import util._
import Helpers._
import xml.{Elem, NodeSeq, Text}

trait BlockUI {

  object reloadAfterSave extends RequestVar[String]("/index")

  /**
   * Creating a Modal Dialog acting as a save confirmation.
   */
  def saveNotificationOK = {
    S.runTemplate(List("_saveNotificationOK_template")).
    map(ns => ModalDialog(ns)) openOr
    Alert("Couldn't find _saveNotificatonOK_template")
  }

  /**
   * Creating the confirm Buttons for the _saveNotificationOK_template.html.
   */
  def confirmSaveNotificationOK(in: NodeSeq) = {
    ("name=yes" #> ((b: NodeSeq) => ajaxButton(b, () => {
         Unblock & RedirectTo(reloadAfterSave)})) &
     "name=no" #> ((b: NodeSeq) => <button onclick={Unblock.toJsCmd}>{b}</button>)
    )(in)
  }

   /**
   * Creating a Modal Dialog acting as a warning confirmation.
   */
  def saveNotificationError = {
    S.runTemplate(List("_saveNotificationError_template")).
    map(ns => ModalDialog(ns)) openOr
    Alert("Couldn't find _saveNotificationError_template")
  }

   /**
   * Creating the confirm Buttons for the _saveNotificationError_template.html.
   */
  def confirmSaveNotificationError(in: NodeSeq) = {
    ("name=no" #> ((b: NodeSeq) => ajaxButton(b, () => {
         Unblock & RedirectTo(reloadAfterSave)})) &
    "name=yes" #> ((b: NodeSeq) => <button onclick={Unblock.toJsCmd}>{b}</button>)
    )(in)
  }
}