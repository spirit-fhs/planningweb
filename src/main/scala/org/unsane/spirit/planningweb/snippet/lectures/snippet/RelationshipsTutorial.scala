package org.unsane.spirit.planningweb.snippet.lectures.snippet

import net.liftweb._
import http._
import common._
import util.Helpers._

/**
 * This class is the representation of the view to create a Relationship for a tutorial
 *
 * @version 1.0
 * @author Christoph Schmidt
 */
class RelationshipsTutorial extends RelationshipHelper with RelationshipsLectureSelect
                                                       with RelationshipsGroupSelect
                                                       with RelationshipsDozentSelect
                                                       with RelationshipsSave {

  // we have to override this value, because the default value is for creating a lecture group
  override val thisSide = "/lecture/tutorialrelation"

  // we have to call this function, because we need a differentiation between
  // a create tutorial process and a create group process
  checkWhereWasTheUserBefore(false)

  def createRelation() = {
     Status.is match {
      case RelationshipHelper.InitialStatus => selectLectureMenue
      case RelationshipHelper.SelectedLecture => addGroups
      case RelationshipHelper.SelectedGroups => addDozents
      case RelationshipHelper.SelectedDozents => saveGroup
    }
  }

  def render = {
    "#create *" #> createRelation
  }
}