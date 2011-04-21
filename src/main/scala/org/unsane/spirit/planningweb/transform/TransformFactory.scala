package org.unsane.spirit.planningweb.transform

/**
 * This object is the representation of the TransformFactory to transform
 * objects form the object-side to the database-side
 *
 * $cs
 * @version 1.0
 *
 * @define cs @author Christoph Schmidt [[mailto:c.schmidt.a@stud.fh-sm.de "
 * <c.schmidt.a@stud.fh-sm.de>]]
 */

import impl._

object TransformFactory {

   /**
   * Create a new TransformDozentType object for different databases. But
   * at the moment is onlay MongoDB supported. It transform the object-world
   * to the database-world
   *
   * @param persType represents the used database, at the moment is only MongoDB supported
   * @return creates a new TransformDozentType object of the used database
   */

  def createTransformDozentType(persType: String) : ITransform = {
    persType match {
      case "mongoDB" => new TransformDozentTypeMongo
      case _ => error("Could not support: " + persType)
    }
  }

   /**
   * Create a new TransformDozent object for different databases. But
   * at the moment is onlay MongoDB supported. It transform the object-world
   * to the database-world
   *
   * @param persType represents the used database, at the moment is only MongoDB supported
   * @return creates a new TransformDozent object of the used database
   */

  def createTransformDozent(persType: String) : ITransform = {
    persType match {
      case "mongoDB" => new TransformDozentMongo
      case _ => error("Could not support: " + persType)
    }
  }

   /**
   * Create a new TransformCourse object for different databases. But
   * at the moment is onlay MongoDB supported. It transform the object-world
   * to the database-world
   *
   * @param persType represents the used database, at the moment is only MongoDB supported
   * @return creates a new TransformCourse object of the used database
   */

  def createTransformCourse(persType: String) : ITransform = {
    persType match {
      case "mongoDB" => new TransformCourseMongo
      case _ => error("Could not support: " + persType)
    }
  }

   /**
   * Create a new TransformLectureType object for different databases. But
   * at the moment is onlay MongoDB supported. It transform the object-world
   * to the database-world
   *
   * @param persType represents the used database, at the moment is only MongoDB supported
   * @return creates a new TransformLectureType object of the used database
   */

  def createTransformLectureType(persType: String) : ITransform = {
    persType match {
      case "mongoDB" => new TransformLectureTypeMongo
      case _ => error("Could not support: " + persType)
    }
  }

   /**
   * Create a new TransformLecture object for different databases. But
   * at the moment is onlay MongoDB supported. It transform the object-world
   * to the database-world
   *
   * @param persType represents the used database, at the moment is only MongoDB supported
   * @return creates a new TransformLecture object of the used database
   */

  def createTransformLecture(persType: String) : ITransform = {
    persType match {
      case "mongoDB" => new TransformLectureMongo
      case _ => error("Could not support: " + persType)
    }
  }

   /**
   * Create a new TransformRoom object for different databases. But
   * at the moment is onlay MongoDB supported. It transform the object-world
   * to the database-world
   *
   * @param persType represents the used database, at the moment is only MongoDB supported
   * @return creates a new TransformRoom object of the used database
   */

  def createTransformRoom(persType: String) : ITransform = {
    persType match {
      case "mongoDB" => new TransformRoomMongo
      case _ => error("Could not support: " + persType)
    }
  }

   /**
   * Create a new TransformFHSDozent object for different databases. But
   * at the moment is onlay MongoDB supported. It transform the object-world
   * to the database-world
   *
   * @param persType represents the used database, at the moment is only MongoDB supported
   * @return creates a new TransformFHSDozent object of the used database
   */

  def createTransformFHSDozent(persType: String) : ITransform = {
    persType match {
      case "mongoDB" => new TransformFHSDozentMongo
      case _ => error("Could not support: " + persType)
    }
  }

   /**
   * Create a new TransformWorktimeManager object for different databases. But
   * at the moment is onlay MongoDB supported. It transform the object-world
   * to the database-world
   *
   * @param persType represents the used database, at the moment is only MongoDB supported
   * @return creates a new TransformWorktimeManager object of the used database
   */

  def createTransformWorktimeManager(persType: String) : ITransform = {
    persType match {
      case "mongoDB" => new TransformWorktimeManagerMongo
      case _ => error("Could not support: " + persType)
    }
  }

   /**
   * Create a new TransformWorktime object for different databases. But
   * at the moment is onlay MongoDB supported. It transform the object-world
   * to the database-world
   *
   * @param persType represents the used database, at the moment is only MongoDB supported
   * @return creates a new TransformWorktime object of the used database
   */

  def createTransformWorktime(persType: String) : ITransform = {
    persType match {
      case "mongoDB" => new TransformWorktimeMongo
      case _ => error("Could not support: " + persType)
    }
  }
}