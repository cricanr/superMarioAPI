package query

object QueryParameters {
  def apply(query: Map[String, Seq[String]]): Option[String] = {
    val maybeSortOrder = getStringParameter(query, "sortOrder")
    maybeSortOrder
  }

  private def getStringParameter(
      query: Map[String, Seq[String]],
      paramName: String
  ): Option[String] = {
    query.get(paramName).flatMap(_.headOption)
  }
}
