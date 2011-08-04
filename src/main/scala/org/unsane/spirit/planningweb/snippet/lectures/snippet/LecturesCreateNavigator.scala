package org.unsane.spirit.planningweb.snippet.lectures.snippet

/**
 * This trait realizes the navigation between the different screens to create or update
 * a Lecture
 *
 * @version 1.0
 * @author Christoph Schmidt
 */

trait LecturesCreateNavigator extends LecturesCreateHelper with LecturesCreateName
                                                  with LecturesCreateCourses
                                                  with LecturesCreateSemesters
                                                  with LecturesCreateDozents
                                                  with LecturesCreateHoures
                                                  with LecturesCreateSave {

/**
  * this function represents the different screens to create or update a lecture
  */
  def navigation = {
    Status.is match {
      case LecturesCreateHelper.InitialStatus => addName
      case LecturesCreateHelper.AddedName => addCourses
      case LecturesCreateHelper.AddedCourse => addSemesters
      case LecturesCreateHelper.AddedSemester => addDozents
      case LecturesCreateHelper.AddedDozent => addHoures
      case LecturesCreateHelper.AddedHoure => saveLecture
    }
  }

}