package org.unsane.spirit.planningweb.snippet.worktimes.snippet

/**
 * This class is the view for the timetablesheduler to manage the wishtimes  of
 *  fhsdozents
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import net.liftweb._
import http._
import common._
import util.Helpers._
import scala.xml._
import js._
import JsCmds._
import JE._
import scala.collection.mutable.Set
import net.liftweb.util.Props
import org.unsane.spirit.planningweb
import planningweb.worktimemanagement.impl._

class WishtimesAdmin extends Wishtimes {

  object FHSDozentName extends SessionVar[String]("")
  status = true
  isFHSDozent = fhsdozents.filter(_.fhsId == FHSDozentName.is)

  // to set the worktime of a dozent
  worktimeOfDozent = isFHSDozent match {
    case List() => List()
    case _ =>  worktimes.filter(_.dozent.name == isFHSDozent.head.dozent.name)
  }

  // to set the notes of a Dozent
  noteTextOfDozent = worktimeOfDozent match {
    case List() => ""
    case _ => worktimeOfDozent.head.notes
  }

  def edit() = {

    val thisSide = "/worktime/edit"

    // to set FHSDozentName
    def setSession(set: String): JsCmd = {
      FHSDozentName(set)
      RedirectTo(thisSide)
    }

    val (name, js) = SHtml.ajaxCall(JE.JsRaw("this.value"),
                                           s => setSession(s))

    // to select a FHSDozent to edit
    def fhsDozentEditChoice() = {
      SHtml.untrustedSelect(("","")::(fhsdozents map (fhsd => (fhsd.fhsId,
                                                               fhsd.fhsId + ": "+ fhsd.dozent.name))),
                            Full(FHSDozentName.is),
                            () => _,
                            "onchange" -> js.toJsCmd)
    }

    fhsDozentEditChoice ++ <br /> ++ select
  }

  override def render = {
    "#edit *" #> edit
  }
}