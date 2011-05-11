package org.unsane.spirit.planningweb.snippet.lectures.snippet

/**
 * This trait is the representation of the view to add some courses to a lecture
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */
import net.liftweb._
import http._
import scala.xml._
import scala.collection.mutable.Set

trait LecturesCreateCourses extends LecturesCreateHelper {

  // this function is used to add some courses to a lecture
  def addCourses() = {

    val courseNames = courses map(_.name)
    val toAdd = Set[String]()

    def next() = {
      toAdd.toList match {
        case List() => S.notice("Please select a course!")
        case _ => Courses(toAdd)
                  Status(LecturesCreateHelper.AddedCourse)
                  S.redirectTo(thisSide)
      }
    }

    def back() = {
      Status(LecturesCreateHelper.InitialStatus)
      S.redirectTo(thisSide)
    }

    val checkboxes = <table id="table-box">
                      <thead>
                       <th>{"Speichern:"}</th>
                       <th>{"Studiengänge:"}</th>
                      </thead>
                      { courseNames.flatMap {
                        name => <tr>
                                 <th>{SHtml.checkbox(if(Courses.is.contains(name)) {
                                                       true
                                                     }
                                                     else {
                                                       false
                                                     }, if (_) toAdd += name)}</th>
                                 <th>{name}</th>
                                </tr>
                        }
                      }
                     </table>

    val nextButton = SHtml.submit("Weiter", next)
    val backButton = SHtml.submit("Zurück", back)
    val cancleButton = SHtml.submit("Abbrechen", cancle)

    val addCourseMenue = checkboxes ++ backButton ++ cancleButton ++ nextButton

    addCourseMenue
  }
}