package org.unsane.spirit.planningweb.snippet

/**
 * This trait is the view to add a name and type of a lecture
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import net.liftweb._
import http._
import common._
import scala.xml._
import scala.collection.mutable.Set
import org.unsane.spirit.planningweb
import planningweb.transform._
import planningweb.persistence._
import planningweb.lecturemanagement.impl._


trait LecturesCreateName extends LecturesCreateHelper {

  // this function represents the screen to select the name and the type of a lecture
  def addName() = {

    val persistenceT:IPersistence = PersistenceFactory
                                      .createPersistence(TransformFactory
                                      .createTransformLectureType(usedPersistence))

    val types = persistenceT.read.asInstanceOf[List[LectureType]]
    var name = ""
    var lectureType = ""


    def next() = {
      Name((name,lectureType))
      Name.is match {
        case (n,t) if n == "" || t == "" => S.notice("Input is wrong or empty!")
        case _ => Status(LecturesCreateHelper.AddedName)
                  S.redirectTo(thisSide)
      }
    }

    val addNameText = SHtml.text(Name.is._1, n => name = n.trim)
    val addTypeChoice = SHtml.untrustedSelect(initialName ::(types map (typeL => (typeL.name,typeL.name))),
                                              Full(Name.is._2),
                                              lectureType = _)
    val nextButton = SHtml.submit("Weiter", next)
    val cancleButton = SHtml.submit("Abbrechen", cancle)

    val addNameMenue = <table id="table-plane">
                        <tr>
                         <th>{"Name:"}</th>
                         <th>{"Typ:"}</th>
                        </tr>
                        <tr>
                         <th>{addNameText}</th>
                         <th>{addTypeChoice}</th>
                        </tr>
                       </table> ++ cancleButton ++ nextButton

    addNameMenue
  }
}