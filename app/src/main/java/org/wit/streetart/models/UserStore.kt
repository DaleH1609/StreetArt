package org.wit.streetart.models

interface UserStore {
    fun findAll(): List<UserModel>
    fun create(user: UserModel)
}