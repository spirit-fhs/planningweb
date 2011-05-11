package org.unsane.spirit.planningweb.snippet.dozents.snippet

/**
 * This trait provides the Dozents view with all necessary informations
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import net.liftweb.util.Props
import org.unsane.spirit.planningweb
import planningweb.dozentmanagement.impl.{Dozent, DozentType, FHSDozent}
import planningweb.lecturemanagement.impl._
import planningweb.worktimemanagement.impl.Worktime
import planningweb.persistence._
import planningweb.transform._

trait DozentsHelper {
  val perLayer = Props.get("spirit.pers.layer") openOr ""

  val persistenceLecture:IPersistence = PersistenceFactory
                                          .createPersistence(TransformFactory
                                          .createTransformLecture(perLayer))

  val persistenceFHSDozent:IPersistence = PersistenceFactory
                                            .createPersistence(TransformFactory
                                            .createTransformFHSDozent(perLayer))

  val persistenceDozent:IPersistence = PersistenceFactory
                                        .createPersistence(TransformFactory
                                        .createTransformDozent(perLayer))

  val persistenceType:IPersistence = PersistenceFactory
                                       .createPersistence(TransformFactory
                                       .createTransformDozentType(perLayer))

  val persistenceWorktime:IPersistence = PersistenceFactory
                                          .createPersistence(TransformFactory
                                          .createTransformWorktime(perLayer))

  val dozentTypes = persistenceType.read.asInstanceOf[List[DozentType]]

  val dozents = persistenceDozent.read.asInstanceOf[List[Dozent]]

  val fhsdozents = persistenceFHSDozent.read.asInstanceOf[List[FHSDozent]]

  val worktimes = persistenceWorktime.read.asInstanceOf[List[Worktime]]

  val lectures = persistenceLecture.read.asInstanceOf[List[Lecture]]

  var usedDozents = List[Dozent]()

  lectures match {
    case List() => usedDozents = List()
    case _ => val listOfDozentInfos = lectures.map(_.courseInfos.map(_.semesterInfos.map(_.dozentInfos))).flatten
              val dozentInfos = listOfDozentInfos.flatten.flatten
              usedDozents = dozentInfos.map(_.dozent).distinct
  }

  fhsdozents match {
    case List() => usedDozents
    case _ => val usedFhsdozents = fhsdozents.map(_.dozent)
              usedDozents = usedDozents.union(usedFhsdozents).distinct
  }

  val alreadyInUse = usedDozents

  def fhsDozentsToUpdate(dozent: Dozent):List[FHSDozent] = fhsdozents.filter(_.dozent.name == dozent.name)

  def worktimesToUpdate(dozent: Dozent):List[Worktime] = worktimes.filter(_.dozent.name == dozent.name)

  val lecturesToUpdate =
    alreadyInUse.map(d =>
      lectures.filter(l => l.courseInfos.head.semesterInfos.head.dozentInfos.exists(_.dozent == d))
    ).flatten

  /**
   * updates a Dozent in a list of CourseInformations
   *
   * @param courseInfos is a list with courseinfos to update
   * @param dozent is the value to update
   * @return a list of updated courseInfos
   */
  def updateCoursInfos(courseInfos: List[CourseInformation], dozent: Dozent): List[CourseInformation] = {
            courseInfos.map(ci => CourseInformation(
                                    ci.course,
                                    ci.semesterInfos
                                      .map(si => SemesterInformation(si.semester,
                                                   si.dozentInfos
                                                     .map(di => DozentInformation(
                                                                  if(di.dozent.name == dozent.name) {dozent} else {di.dozent},
                                                                  di.giveLecture,
                                                                  di.giveTutorial))))))
  }

  /**
   * updates a Dozent in a list of LectureRelationships
   *
   * @param relations is a list with LectureRelationships to update
   * @param dozent is the value to update
   * @return a list of updated LectureRelationships
   */
  def updateLectureRelationships(relations: List[LectureRelationship], dozent: Dozent): List[LectureRelationship] = {
            relations.map(r => LectureRelationship(r.these,
                                                   r.withThose.map(d => if(d.name == dozent.name) {
                                                                          dozent
                                                                        } else {d})))
  }
}