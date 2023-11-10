##Asumptions
* POST entity
  * This entity doesn't support hard delete
  * We can reset the content of post to empty string but wont delete it.
  
* Comment entity
  * This entity doesn't support hard delete
  * We can reset the content of comment to empty string but wont delete it.
  
* While deleting the post, soft delete will happen. Which means after post is deleted it’s associated comment will stay.

* While deleting the comment, soft delete will happen. There won’t be any backup for the previous comment.
  * set content= “This comment is deleted”
  * set isDeleted=true
  * set associated like/dislike count as 0 for the given comment-id