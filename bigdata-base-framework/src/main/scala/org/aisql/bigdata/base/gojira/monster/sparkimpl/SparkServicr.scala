package org.aisql.bigdata.base.gojira.monster.sparkimpl

import org.aisql.bigdata.base.gojira.monster.Ancestor
import org.aisql.bigdata.base.gojira.enum.MonsterType
import org.aisql.bigdata.base.gojira.enum.MonsterType.MonsterType
import org.aisql.bigdata.base.gojira.model.ClassModel
import org.aisql.bigdata.base.util.DateUtil

/**
  * Author: xiaohei
  * Date: 2019/9/15
  * Email: xiaohei.info@gmail.com
  * Host: xiaohei.info
  */
class SparkServicr(basePackage: String, whoami: String) extends Ancestor {

  logger.info(s"${this.getClass.getSimpleName} init")

  private val bottomPkgName = "sparkimpl"

  override val monsterType: MonsterType = MonsterType.SPARK_SERVICE

  override protected var pkgName: String = s"package $basePackage.service.$bottomPkgName"

  override protected var impPkgs: String = _

  override protected var author: String =
    s"""
       |/**
       |  * Author: $whoami
       |  * Date: ${DateUtil.currTime}
       |  * CreateBy: @${this.getClass.getSimpleName}
       |  *
       |  */
      """.stripMargin

  /**
    * 类初始化设置
    *
    * 设置classModel相关字段
    **/
  override def init(): Unit = {

    val beanClsName = s"${baseClass}Bean"
    val daoClsName = s"${baseClass}SparkDao"

    impPkgs =
      s"""
         |import $frameworkPackage.hive.BaseHiveDao
         |import $frameworkPackage.hive.BaseHiveService
         |import $basePackage.dal.bean.$beanClsName
         |import $basePackage.dal.dao.$bottomPkgName.$daoClsName

         |import org.apache.spark.rdd.RDD
         |import org.apache.spark.sql.SparkSession
    """.stripMargin

    val fields: String =
      s"""
         |  protected override val dao: BaseHiveDao[SparkSession, RDD[$beanClsName]] = new $daoClsName
    """.stripMargin

    classModel = initClassModel
    classModel.setImport(impPkgs)
    classModel.setAuthor(author)
    classModel.setFields(fields)
    logger.info(s"$monsterType class model done")
  }

  private def initClassModel: ClassModel = {
    val clsHeader: String =
      s"""
         |class $baseClass$monsterType extends BaseHiveService[SparkSession, RDD[${baseClass}Bean]]""".stripMargin
    new ClassModel(pkgName, clsHeader)
  }

}