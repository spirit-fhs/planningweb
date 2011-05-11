package org.unsane.spirit.planningweb.snippet

/**
 * This class creates the different views to build a group
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


class RelationshipsLecture extends RelationshipHelper with RelationshipsLectureSelect
                                                      with RelationshipsGroupSelect
                                                      with RelationshipsDozentSelect
                                                      with RelationshipsSave {

  // we have to call this function, because we need a differentiation between
  // a create tutorial process and a create group process
  checkWhereWasTheUserBefore(true)

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