package org.unsane.spirit.planningweb


/**
 * Developer: Christoph Schmidt
 */
import net.liftweb.util.Props
import org.specs._
import persistence._
import net.liftweb.mongodb._
import coursemanagement.impl._
import transform._
import scala.collection.immutable.List

object CourseManagementSpecs extends Specification("CourseManagement Specification") {

  val usedPersistence = Props.get("spirit.pers.layer") openOr ""

  usedPersistence match {
    case "mongoDB" =>
      MongoDB.defineDbAuth(DefaultMongoIdentifier,
        MongoAddress(MongoHost("127.0.0.1", 27017), "spirit_curriculum"),
        "spirit_curriculum",
        "spirit_curriculum")
      case _ =>
  }

  val semesters = List(Semester(1,10),
                       Semester(2,5),
                       Semester(3,0),
                       Semester(4,9),
                       Semester(5,20),
                       Semester(6,50))

  val semestersUpdate = List(Semester(1,0),
                             Semester(2,0),
                             Semester(3,0),
                             Semester(4,0),
                             Semester(5,0),
                             Semester(6,0))

  val course = Course("TestCourse", "TC", semesters)

  val courseUpdate = Course("TestCourseUpdate", "TCU", semestersUpdate)

  "Course" should {
    "when add a course" >> {
      val persistence:IPersistence = PersistenceFactory.createPersistence(TransformFactory
                                                       .createTransformCourse(usedPersistence))

      persistence.create(course)
      "contain this course in persistence" >> {
        val courses = persistence.read.asInstanceOf[List[Course]]
        courses contains(course) must_== true
      }
    }
    "when update a course" >> {
      val persistence:IPersistence = PersistenceFactory.createPersistence(TransformFactory
                                                       .createTransformCourse(usedPersistence))
      persistence.update(course,courseUpdate)
      "contain the updated course in persistence" >> {
        val updatedCourses = persistence.read.asInstanceOf[List[Course]]
        updatedCourses contains(courseUpdate) must_== true
      }
    }
    "When delete a course" >> {
      val persistence:IPersistence = PersistenceFactory.createPersistence(TransformFactory
                                                       .createTransformCourse(usedPersistence))
      persistence.delete(courseUpdate)
      "not contain the deleted course in persistence" >> {
        val courses = persistence.read.asInstanceOf[List[Course]]
        courses contains(courseUpdate) must_== false
      }
    }
  }
}