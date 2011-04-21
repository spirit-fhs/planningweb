package org.unsane.spirit.planningweb.snippet

/**
 * This trait is the view to save a group
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
import planningweb.lecturemanagement.impl._
import planningweb.coursemanagement.impl._


// this trait represents the screen to save a Group
trait RelationshipsSave extends RelationshipHelper {
  // this is the save function of a Group
  def saveGroup() = {

    val courseSemesters = Groups.is.toList.map(cs => CourseSemester(cs._1,cs._2))

    var toSave:Box[Lecture] = Empty

    if(IsTutorial.is == false) {
      toSave = Full(Lecture(ToAddRelation.is.head.name,
                            ToAddRelation.is.head.lectureType,
                            ToAddRelation.is.head.courseInfos,
                            (LectureRelationship(courseSemesters,
                                                 Dozents.is.toList))::ToAddRelation.is.head.hasLectureTogetherWith,
                            ToAddRelation.is.head.hasTutorialTogetherWith,
                            ToAddRelation.is.head.hoursOfLecture,
                            ToAddRelation.is.head.hoursOfTutorial,
                            ToAddRelation.is.head.inSummerSemester,
                            ToAddRelation.is.head.inWinterSemester
                           ))
    } else {
        toSave = Full(Lecture(ToAddRelation.is.head.name,
                              ToAddRelation.is.head.lectureType,
                              ToAddRelation.is.head.courseInfos,
                              ToAddRelation.is.head.hasLectureTogetherWith,
                              (LectureRelationship(courseSemesters,
                                                   Dozents.is.toList))::ToAddRelation.is.head.hasTutorialTogetherWith,
                              ToAddRelation.is.head.hoursOfLecture,
                              ToAddRelation.is.head.hoursOfTutorial,
                              ToAddRelation.is.head.inSummerSemester,
                              ToAddRelation.is.head.inWinterSemester
                             ))
      }


    def save() = {
      toSave match {
        case _ if !lectures.contains(ToAddRelation.is.head) => persistence.create(toSave.head)
        case _ => persistence.update(ToAddRelation.is.head,toSave.head)
      }
      ToAddRelation(Empty)
      Groups(initialGroups)
      Dozents(initialDozents)
      Status(RelationshipHelper.InitialStatus)
      IsTutorial(false)
      S.redirectTo(lectureManagement)
    }

    def back() = {
      Status(RelationshipHelper.SelectedGroups)
      S.redirectTo(thisSide)
    }

    val saveButton = SHtml.submit("Speichern", save)
    val backButton = SHtml.submit("Zurück", back)
    val cancleButton = SHtml.submit("Abbrechen", cancle)

    val saveGroupMenue = <table id="table-box">
                          <thead>
                           <th>{"Gruppe:"}</th>
                           <th>{"Dozent:"}</th>
                          </thead>
                          <tr>
                           <th>{if (IsTutorial.is == false ) {
                                  toSave.head
                                        .hasLectureTogetherWith
                                        .head
                                        .these
                                        .map(ci => ci.semester.toString + ". " + ci.course ++ <br />)
                                } else {
                                    toSave.head
                                        .hasTutorialTogetherWith
                                        .head
                                        .these
                                        .map(ci => ci.semester.toString + ". " + ci.course ++ <br />)
                                  }
                               }
                           </th>
                           <th>{if (IsTutorial.is == false) {
                                  toSave.head
                                        .hasLectureTogetherWith
                                        .head
                                        .withThose
                                        .map(d => d.name ++ <br />)
                                } else {
                                    toSave.head
                                        .hasTutorialTogetherWith
                                        .head
                                        .withThose
                                        .map(d => d.name ++ <br />)
                                  }
                               }
                           </th>
                          </tr>
                         </table> ++ backButton ++ cancleButton ++ saveButton


    saveGroupMenue
  }
}