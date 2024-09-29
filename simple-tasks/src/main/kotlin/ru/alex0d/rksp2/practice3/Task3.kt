package ru.alex0d.rksp2.practice3

import io.reactivex.rxjava3.core.Observable
import kotlin.random.Random

data class UserFriend(
    val userId: Int,
    val friendId: Int
)

fun getFriends(userId: Int, friends: List<UserFriend>): Observable<UserFriend> {
    return Observable.fromIterable(friends)
        .filter { it.userId == userId }
}

fun main() {
    val userFriends = List(1000) {
        UserFriend(
            userId = Random.nextInt(100),
            friendId = Random.nextInt(100)
        )
    }

    val userIds = List(100) { Random.nextInt(100) }

    val friendsObservable = Observable.fromIterable(userIds)
        .flatMap { getFriends(it, userFriends) }

    friendsObservable.subscribe { println(it) }
}