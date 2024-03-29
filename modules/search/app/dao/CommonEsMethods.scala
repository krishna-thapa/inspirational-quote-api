package dao

import com.krishna.util.Logging
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.Response
import com.sksamuel.elastic4s.requests.indexes.CreateIndexResponse
import com.sksamuel.elastic4s.requests.indexes.admin.DeleteIndexResponse
import com.sksamuel.elastic4s.requests.mappings.{ CompletionField, MappingDefinition, TextField }
import config.InitEs

import scala.concurrent.Future

trait CommonEsMethods extends InitEs with Logging {

  /*
    Count the total docs inside the index, used for testing
   */
  def countDocsInIndex: Long = {
    client
      .execute {
        count(indexName)
      }
      .await
      .result
      .count
  }

  /**
    * Delete the index in elastic search with given index name
    * @param indexName -> Index to delete
    * @return -> Future response
    */
  def deleteQuotesIndex(indexName: String): Future[Response[DeleteIndexResponse]] = {
    log.warn(s"Deleting the index: $indexName")
    client.execute(
      deleteIndex(indexName)
    )
  }

  /**
    * Check if the index is present in the ElasticSearch
    * @return a boolean
    */
  def doesIndexExists: Boolean = {
    log.info(s"Checking if the index: $indexName exists already")
    client
      .execute {
        indexExists(indexName)
      }
      .await
      .result
      .isExists
  }

  /*
    Create an index and a custom field with the type of completion field and copy the author data on it
    We can copy next data on that completion field so that can be used for auto completion
    https://www.elastic.co/guide/en/elasticsearch/reference/5.5/copy-to.html
   */
  def createIndexWithCompletionField: Future[Response[CreateIndexResponse]] = {
    log.info(s"Creating the index: $indexName with author as a completion field")
    client.execute {
      createIndex(indexName).mapping(
        MappingDefinition(
          Seq(CompletionField("suggest_author"), TextField("author").copyTo("suggest_author"))
        )
      )
    }
  }
}
