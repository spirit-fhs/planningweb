package org.unsane.spirit.planningweb.snippet

/**
 * This class is the representation of the view to manage the Dozents
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
import util.Helpers._
import scala.xml._
import net.liftweb.util.Props
import js._
import JsCmds._
import JE._
import org.unsane.spirit.planningweb
import planningweb.dozentmanagement.impl.{Dozent, DozentType, FHSDozent}
import planningweb.lecturemanagement.impl._
import planningweb.worktimemanagement.impl.Worktime
import planningweb.persistence._
import planningweb.transform._

// this snippet is the menue to create, update and delete a dozent
class Dozents extends DozentsHelper {

  // to sotre a dozent
  def add () = {

    var name = ""
    var reasonSelfManagement = ""
    var timeSelfManagement = "0.0"
    var typeName = ""
    var dozentName = ""
    val initTime = "0.0"

    // to store a dozent object
    def addDozent() {
      val alreadyAvailable = dozents.filter {_.name == name}

       if (name != "" &&
          typeName != "" &&
          alreadyAvailable.isEmpty) {
        val dozentType = dozentTypes.filter (typeD => typeD.name == typeName).head
        try {
          persistenceDozent.create(Dozent(name, reasonSelfManagement, timeSelfManagement.toDouble, dozentType))
        } catch {
            case e: Exception => S.notice(timeSelfManagement + " is not a Number!")
          }
      }
      else {
        S.notice("Dozent already exist or Input is empty!")
      }
    }

    // to select a type for a dozent
    def typeChoice() : Elem = {
      val first = dozentTypes match {
        case List() => Empty
        case _ => Full(dozentTypes.head.name)
      }
      SHtml.untrustedSelect(dozentTypes map (typeD => (typeD.name, typeD.name)),
                            first,
                            typeName = _)
    }

    // to select a type for a dozent
    val typeToAddChoice = typeChoice
    // to add a name for a dozent
    val nameToAddText = SHtml.text("", n => name = n.trim)
    // to add a reason for a dozent
    val reasonSelfMtoAddText = SHtml.text("", r => reasonSelfManagement = r.trim)
    // to add a time for a dozent
    val timeSelfMtoAddText = SHtml.text(initTime, t =>  timeSelfManagement = t.trim)
    // a button to store a dozent object
    val addButton = SHtml.submit("Hinzufügen", addDozent)

    // the menue to add a dozent
    val addMenue = <table id="talbe-plane">
                    <tr>
                     <th>
                       {"Name:"}<br />{nameToAddText}
                     </th>
                     <th>
                       {"Typ:"}<br />{typeToAddChoice}
                     </th>
                    </tr>
                    <tr>
                     <th>
                       {"Selbstmanagement:"}<br />{timeSelfMtoAddText}
                     </th>
                     <th>
                       {"Grund:"}<br />{reasonSelfMtoAddText}
                     </th>
                    </tr>
                   </table> ++ addButton

    addMenue
  }

  // to delete a dozent
  def delete() = {

    import scala.collection.mutable.Set
    val toDelete = Set[Dozent]()
    // to delete a dozent object
    def deleteDozents(toDelete: Set[Dozent]) = {
      toDelete foreach {
        dozent => alreadyInUse match {
                    case _ if(!alreadyInUse.contains(dozent)) => persistenceDozent.delete(dozent)
                    case _ => S.warning("Could not delete dozent, because " + dozent.name + " is already in use!")
                  }
      }
    }
    // to select a dozent to delete
    val checkboxes = <table id="table-box">
                       <thead>
                        <th>{"Löschen:"}</th>
                        <th>{"Name:"}</th>
                        <th>{"Typ:"}</th>
                        <th>{"Pflicht-Std.:"}</th>
                        <th>{"Selbst-Mngt.:"}</th>
                        <th>{"Grund:"}</th>
                        <th>{"Lehrauftrag:"}</th>
                       </thead>
                       { dozents.flatMap {
                         dozent => <tr>
                                    <th>{SHtml.checkbox(false, if (_) toDelete += dozent)}</th>
                                    <th>{dozent.name}</th>
                                    <th>{dozent.typeD.name}</th>
                                    <th>{dozent.typeD.requiredTime}</th>
                                    <th>{dozent.timeSelfManagement}</th>
                                    <th>{dozent.reasonSelfManagement}</th>
                                    <th>{dozent.typeD.hasLectureship}</th>
                                   </tr>
                         }
                       }
                     </table>

    val delete = SHtml.submit("Löschen", () => deleteDozents(toDelete))
    checkboxes ++ delete
  }

  // to update a Dozent
  def update () = {

    var reasonSelfManagement = ""
    var timeSelfManagement = ""
    var typeName = ""

    object DozentName extends SessionVar[String]("")
    val thisSide = "/dozent/dozent"
    // to set DozentName
    def setSession(set: String): JsCmd = {
      DozentName(set)
      RedirectTo(thisSide)
    }

    val (name2, js) = SHtml.ajaxCall(JE.JsRaw("this.value"),
                                           s => setSession(s))
    // to update a dozent object
    def updateDozent() {

      if (DozentName.is != "" && typeName != "") {

        val before = dozents.filter {_.name == DozentName.is}.head
        val dozentType = dozentTypes.filter (typeD => typeD.name == typeName).head
        val dozentToUpdate =  Dozent(DozentName.is,
                                          reasonSelfManagement,
                                          timeSelfManagement.toDouble,
                                          dozentType)

        try {
          persistenceDozent.update(before,dozentToUpdate)

          fhsDozentsToUpdate(before).map(fd => persistenceFHSDozent
                                                 .update(fd, FHSDozent(fd.fhsId,dozentToUpdate)))

          worktimesToUpdate(before).map(wt => persistenceWorktime
                                                .update(wt, Worktime(dozentToUpdate,wt.timeSlots,wt.rooms)))

          lecturesToUpdate.map(l => persistenceLecture
                                      .update(l,Lecture(l.name,
                                                        l.lectureType,
                                                        updateCoursInfos(l.courseInfos, dozentToUpdate),
                                                        updateLectureRelationships(l.hasLectureTogetherWith, dozentToUpdate),
                                                        updateLectureRelationships(l.hasTutorialTogetherWith, dozentToUpdate),
                                                        l.hoursOfLecture,
                                                        l.hoursOfTutorial,
                                                        l.inSummerSemester,
                                                        l.inWinterSemester)))
        } catch {
            case e: Exception => S.notice(timeSelfManagement + " is not a Number!")
          }
      }
      else {
        S.notice("No dozent or type selected!")
      }
    }
    // to select a dozent to update a dozent object
    def dozentUpdateChoice() = {
      SHtml.untrustedSelect(("","")::(dozents map (dozent => (dozent.name, dozent.name))),
                            Full(DozentName.is),
                            () => _,
                            "onchange" -> js.toJsCmd)
    }
    // to select a type to update a dozent object
      def typeUpdateChoice() = {
      SHtml.untrustedSelect(("","") :: (dozentTypes map (typeD => (typeD.name, typeD.name))),
                                        if (DozentName.is == "") {
                                          Full("")
                                        }
                                        else {
                                          val preview = dozents.filter(dozent => dozent.name == DozentName.is)
                                          dozents match {
                                            case List() => Full("")
                                            case _ if preview.isEmpty
                                              => Full("")
                                            case _ =>
                                              Full(preview.head
                                                          .typeD
                                                          .name)
                                          }
                                        },
                                       typeName = _)
    }
    // to add time to update a dozent object
    def selfManagementUpdateText() = {
      SHtml.text(if (DozentName.is == "") {
                   ""
                 }
                 else {
                   val preview = dozents.filter(dozent => dozent.name == DozentName.is)
                   dozents match {
                     case List() => ""
                     case _ if preview.isEmpty
                       => ""
                     case _ =>
                       preview.head
                              .timeSelfManagement
                              .toString
                   }
                 }, t => timeSelfManagement = t.trim)
    }
    // to add a reason to update a dozent object
    def reasonUpdateText() = {
      SHtml.text(if (DozentName.is == "") {
                   ""
                 }
                 else {
                   val preview = dozents.filter(dozent => dozent.name == DozentName.is)
                   dozents match {
                     case List() => ""
                     case _ if preview.isEmpty
                       => ""
                     case _ =>
                       preview.head
                              .reasonSelfManagement
                   }
                 }, r => reasonSelfManagement = r.trim)
    }
    // to build a update button to call the update function for a dozent object
    def updateButton() = {
      SHtml.submit("Ändern", updateDozent)
    }
    // the menue to update a dozent
    val updateMenue =
      <table id="table-plane">
       <tr>
        <th>
         {"Name:"}<br />{dozentUpdateChoice}
        </th>
        <th>
         {"Typ:"}<br />{typeUpdateChoice}
        </th>
       </tr>
        <tr>
         <th>
          {"Selbstmanagement:"}<br />{selfManagementUpdateText}
         </th>
         <th>
          {"Grund:"}<br />{reasonUpdateText}
         </th>
        </tr>
       </table> ++ updateButton

    updateMenue
  }

  // to render the dozent menue
  def render = {
    "#add *" #> add &
    "#delete *" #> delete &
    "#update *" #> update
  }
}