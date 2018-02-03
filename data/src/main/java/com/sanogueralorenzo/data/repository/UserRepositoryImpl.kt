package com.sanogueralorenzo.data.repository

import com.sanogueralorenzo.data.cache.Cache
import com.sanogueralorenzo.data.model.UserEntity
import com.sanogueralorenzo.data.model.UserMapper
import com.sanogueralorenzo.data.remote.UsersApi
import com.sanogueralorenzo.domain.model.User
import com.sanogueralorenzo.domain.repository.UserRepository
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(private val api: UsersApi,
                                             private val cache: Cache<List<UserEntity>>,
                                             private val mapper: UserMapper) : UserRepository {
    private val key = "User List"

    override fun getUsers(): Flowable<List<User>> = Single.concat(getCache(), getRemote())

    override fun getUser(userId: String): Flowable<User> = Single.concat(getCache(userId), getRemote(userId))

    private fun getRemote(): Single<List<User>> = api.getUsers().doAfterSuccess { setCache(it) }.map { mapper.mapToDomain(it) }

    private fun getCache(): Single<List<User>> = cache.load(key, listOf()).map { mapper.mapToDomain(it) }

    private fun getRemote(userId: String): Single<User> = api.getUser(userId).doAfterSuccess { setCache(it) }.map { mapper.mapToDomain(it) }

    private fun getCache(userId: String): Single<User> = cache.load(key, listOf()).map { mapper.mapToDomain(it) }.map { it.first { it.id == userId } }

    private fun setCache(list: List<UserEntity>) = cache.save(key, list).subscribe()

    private fun setCache(user: UserEntity) = cache.load(key, listOf()).map { it.filter { it.id != user.id } }.doAfterSuccess { setCache(it) }.subscribe()
}
