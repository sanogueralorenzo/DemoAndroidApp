@file:Suppress("IllegalIdentifier")

package com.sanogueralorenzo.posts.presentation.model

import com.sanogueralorenzo.posts.comment
import org.junit.Assert.assertTrue
import org.junit.Test

class CommentItemTest {

    @Test
    fun `map domain to presentation`() {
        // given

        // when
        val commentItem = mapToPresentation(listOf(comment)).first()

        // then
        assertTrue(commentItem.postId == comment.postId)
        assertTrue(commentItem.id == comment.id)
        assertTrue(commentItem.name == comment.name)
        assertTrue(commentItem.email == comment.email)
        assertTrue(commentItem.body == comment.body)
    }
}
