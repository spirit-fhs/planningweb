package org.unsane.spirit.planningweb


/**
 * Developer: Christoph Schmidt
 */

import net.liftweb.util.Props
import org.specs._
import persistence._
import net.liftweb.mongodb._
import lecturemanagement.impl._
import coursemanagement.impl._
import dozentmanagement.impl._
import transform._
import snippet.PlanningLectureCreater

object LectureManagementSpecs extends Specification("LectureManagement Specification") {

  val usedPersistence = Props.get("spirit.pers.layer") openOr ""

  usedPersistence match {
    case "mongoDB" =>
      MongoDB.defineDbAuth(DefaultMongoIdentifier,
        MongoAddress(MongoHost("127.0.0.1", 27017), "spirit_curriculum"),
        "spirit_curriculum",
        "spirit_curriculum")
      case _ =>
  }

  val dozentType =  DozentType("TestType",5,true)
  val dozentTypeUpdate =  DozentType("TestTypeUpdate",1,false)

  val dozent = Dozent("TestDozent", "self-management", 2.5, dozentType)
  val dozentUpdate = Dozent("TestDozentUpdate", "self-managementUpdate", 1.5, dozentTypeUpdate)

  val lectureType = LectureType("TestLectureType")
  val lectureTypeUpdate = LectureType("TestLectureTypeUpdate")

  val lectureRelationShips = List(LectureRelationship(List(CourseSemester("TestCourse",1),
                                                           CourseSemester("TestCourse",2)),
                                                      List(dozent)))

  val lectureRelationShipsUpdate = List(LectureRelationship(List(CourseSemester("TestCourseUpdate",0),
                                                                 CourseSemester("TestCourseUpdate",0)),
                                                            List(dozentUpdate)))

  val semesters = List(Semester(1,10),
                       Semester(2,5),
                       Semester(3,0),
                       Semester(4,9),
                       Semester(5,20),
                       Semester(6,50))

  val semestersUpdate = List(Semester(0,0),
                             Semester(0,0),
                             Semester(0,0),
                             Semester(0,0),
                             Semester(0,0),
                             Semester(0,0))

  val course = Course("TestCourse", "TC", semesters)
  val courseUpdate = Course("TestCourseUpdate", "TCU", semestersUpdate)

  val dozentInfos = List(DozentInformation(dozent,true,true))
  val dozentInfosUpdate = List(DozentInformation(dozentUpdate,false,false))

  val semesterInfos = List(SemesterInformation(1,dozentInfos))
  val semesterInfosUpdate = List(SemesterInformation(2,dozentInfosUpdate))

  val courseInfos = List(CourseInformation(course,
                                           semesterInfos))

  val courseInfosUpdate = List(CourseInformation(courseUpdate,
                                                 semesterInfosUpdate))


  val lecture = Lecture("TestLecture",
                        lectureType,
                        courseInfos,
                        lectureRelationShips,
                        lectureRelationShips,
                        1,
                        1,
                        false,
                        false)

  val lectureUpdate = Lecture("TestLectureUpdate",
                              lectureTypeUpdate,
                              courseInfosUpdate,
                              lectureRelationShipsUpdate,
                              lectureRelationShipsUpdate,
                              2,
                              2,
                              false,
                              false)

  "LectureType" should {
    "when add a lecture-type" >> {
      val persistence:IPersistence = PersistenceFactory
                                       .createPersistence(TransformFactory
                                       .createTransformLectureType(usedPersistence))
      persistence create(lectureType)
      "contain this lecture-type in persistence" >> {
        val lectureTypes = persistence.read.asInstanceOf[List[LectureType]]
        lectureTypes contains(lectureType) must_==  true
      }
    }
    "when update a lecture-type" >> {
      val persistence:IPersistence = PersistenceFactory
                                      .createPersistence(TransformFactory
                                      .createTransformLectureType(usedPersistence))
      persistence.update(lectureType,lectureTypeUpdate)
      "contain the updated lecture-type in persistence" >> {
        val lectureTypes = persistence.read.asInstanceOf[List[LectureType]]
        lectureTypes contains(lectureTypeUpdate) must_==  true
      }
    }
    "when delete a lecture-type" >> {
      val persistence:IPersistence = PersistenceFactory
                                      .createPersistence(TransformFactory
                                      .createTransformLectureType(usedPersistence))
      persistence.delete(lectureTypeUpdate)
      "not contain the deleted lecture-type in persistence" >> {
        val lectureTypes = persistence.read.asInstanceOf[List[LectureType]]
        lectureTypes contains(lectureTypeUpdate) must_==  false
      }
    }
    "When delete a lecture-type which is allready in use" >> {
       val persistenceT:IPersistence = PersistenceFactory
                                        .createPersistence(TransformFactory
                                        .createTransformLectureType(usedPersistence))
       val persistenceL:IPersistence = PersistenceFactory
                                        .createPersistence(TransformFactory
                                        .createTransformLecture(usedPersistence))
       persistenceT create(lectureTypeUpdate)
       persistenceL create(lectureUpdate)
       persistenceT delete(lectureTypeUpdate)
      "contain this lecture-type in persistence" >> {
        val lectureTypes = persistenceT.read.asInstanceOf[List[LectureType]]
        lectureTypes contains(lectureTypeUpdate) must_==  true

        persistenceL.delete(lectureUpdate)
        persistenceT.delete(lectureTypeUpdate)
      }
    }
  }

  "Lecture" should {
    "when add a lecture" >> {
      val persistence:IPersistence = PersistenceFactory
                                      .createPersistence(TransformFactory
                                      .createTransformLecture(usedPersistence))
      persistence create(lecture)
      "contain this lecture in persistence" >> {
        val lectures = persistence.read.asInstanceOf[List[Lecture]]
        lectures contains(lecture) must_==  true
      }
    }
    "when update a lecture"  >> {
      val persistence:IPersistence = PersistenceFactory
                                      .createPersistence(TransformFactory
                                      .createTransformLecture(usedPersistence))

      persistence.update(lecture,lectureUpdate)
      "contain the updated lecture in persistence" >> {
        val lectures = persistence.read.asInstanceOf[List[Lecture]]
        lectures contains(lectureUpdate) must_==  true
      }
    }
    "when delete a lecture" >> {
      val persistence:IPersistence = PersistenceFactory
                                       .createPersistence(TransformFactory
                                       .createTransformLecture(usedPersistence))

      persistence.delete(lectureUpdate)
      "not contain the deleted lecture in persistence" >> {
        val lectures = persistence.read.asInstanceOf[List[Lecture]]
        lectures contains(lectureUpdate) must_==  false
      }
    }
  }
  "When call buildPlan with a list of lectures from PlanningLectureCreater " +
  "and with empty lists for hasLectureTogetherWith and hasTutorialTogetherWith" >> {
    val result = PlanningLectureCreater.buildPlan( List(Lecture("TestLecture",
                                                                lectureType,
                                                                courseInfos,
                                                                List(),
                                                                List(),
                                                                2,
                                                                1,
                                                                false,
                                                                false)))
    "the functon returns a list with PlanningLectures" >> {
      result == List(PlanningLecture("TestLecture",
                                     "Vorlesung",
                                     List(CourseSemester("TestCourse",1)),
                                     List(Dozent("TestDozent", "self-management", 2.5, dozentType)),
                                     94),
                     PlanningLecture("TestLecture",
                                     "Vorlesung",
                                     List(CourseSemester("TestCourse",1)),
                                     List(Dozent("TestDozent", "self-management", 2.5, dozentType)),
                                     94),
                     PlanningLecture("TestLecture",
                                     "Übung",
                                     List(CourseSemester("TestCourse",1)),
                                     List(Dozent("TestDozent", "self-management", 2.5, dozentType)),
                                     94)) must_==  true
    }
  }
  "When call buildPlan with a list of lectures from PlanningLectureCreater " +
  "and with no empty lists for hasLectureTogetherWith and hasTutorialTogetherWith" >> {
    val result = PlanningLectureCreater.buildPlan(List(lecture))
    "the function returns a list with PlanningLectures" >> {
      result == List(PlanningLecture("TestLecture",
                                     "Vorlesung",
                                     List(CourseSemester("TestCourse",1),
                                          CourseSemester("TestCourse",2)),
                                     List(Dozent("TestDozent",
                                                 "self-management",2.5,
                                                 DozentType("TestType",5,true))),
                                     94),
                     PlanningLecture("TestLecture",
                                     "Übung",
                                      List(CourseSemester("TestCourse",1),
                                           CourseSemester("TestCourse",2)),
                                      List(Dozent("TestDozent",
                                                  "self-management",2.5,
                                                  DozentType("TestType",5,true))),
                                      94)) must_== true
    }
  }
}