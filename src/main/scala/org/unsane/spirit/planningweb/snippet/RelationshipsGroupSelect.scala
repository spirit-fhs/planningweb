package org.unsane.spirit.planningweb.snippet

/**
 * This trait is the view to select members for a group
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

// this trait represents the screen to select members for a group
trait RelationshipsGroupSelect extends RelationshipHelper {

  // this function creates the menue to seclect the member of a group
  def addGroups() = {
    val minMembers = 1
    val toAdd = Set[(GroupName,SemesterNumber)]()

    var existingGroups = List[List[(String,Int)]]()

    if(IsTutorial.is == false) {
      existingGroups = ToAddRelation.is.head.hasLectureTogetherWith.map(
                          _.these.map(cs => (cs.course,cs.semester))
                       )
    } else {
        existingGroups = ToAddRelation.is.head.hasTutorialTogetherWith.map(
                            _.these.map(cs => (cs.course,cs.semester))
                         )
      }


    val allCourseSemester = (ToAddRelation.is.head.courseInfos map (
                              ci=> ci.semesterInfos map(si => (ci.course.name,si.semester))
                            )).flatten

    val courseSemester = allCourseSemester.diff(existingGroups.flatten)

    def next() = {
      toAdd.toList match {
        case List() => S.notice("Please select min. 1 members!")
        case _ if toAdd.size < minMembers => S.notice("Not enough members selected!")
        case _ => Groups(toAdd)
                  Status(RelationshipHelper.SelectedGroups)
                  S.redirectTo(thisSide)
      }
    }

    def back() = {
      Status(RelationshipHelper.InitialStatus)
      S.redirectTo(thisSide)
    }

    val linesOfgroups = existingGroups.map(eg => <th>
                                                  {eg.map(cs => cs._1 + " " + cs._2.toString ++ <br />)}
                                                 </th>)

    val tablesOfGroups = linesOfgroups.map(lg => <table>
                                                  <tr>
                                                   <th>{"Gruppe:"}</th>
                                                    {lg}
                                                  </tr>
                                                 </table>)

    val checkboxes = <table id="table-box">
                      <thead>
                       <th>{"Speichern:"}</th>
                       <th>{"Semester:"}</th>
                      </thead>
                      { courseSemester.sorted.flatMap {
                        cs => <tr>
                               <th>{SHtml.checkbox(if (Groups.is.contains((cs._1,cs._2))) {
                                                     true
                                                   }
                                                   else {
                                                     false
                                                   }, if (_) toAdd += cs)}</th>
                               <th>{cs._2.toString + ". " + cs._1}</th>
                              </tr>
                        }
                      }
                     </table>

    val groupViews = tablesOfGroups.foldLeft(checkboxes.asInstanceOf[NodeSeq])(_ ++ _)

    val nextButton = SHtml.submit("Weiter", next)
    val backButton = SHtml.submit("Zurück", back)
    val cancleButton = SHtml.submit("Abbrechen", cancle)

    val addGroupMenue = groupViews ++ backButton ++ cancleButton ++ nextButton

    addGroupMenue
  }
}