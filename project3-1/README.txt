We created Lucene indexes on the following attributes:
(1) Item ID
(2) Name
(3) UNION (Name, Category, Description) 

The indexes on both Item ID and Name are StringField because they are atomic values that should not be tokenized. We store them because we can retrieve the corresponding ids and names from the database later once we obtain the query result list from the Lucene index. 
As for the index on UNION represents the union of of the keywords in the name, category and description attributes. It is of TextField since it should be parsed (or "tokenized") into a set of words for indexing. We don't store it because it doesn't need to be displayed in the query results.