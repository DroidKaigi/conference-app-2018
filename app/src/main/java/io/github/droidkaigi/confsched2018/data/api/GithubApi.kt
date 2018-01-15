package io.github.droidkaigi.confsched2018.data.api

import io.github.droidkaigi.confsched2018.data.api.response.Contributor
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {
    @GET("/repos/{owner}/{repo}/contributors")
    fun getContributors(@Path("owner") owner: String, @Path("repo") repo: String,
                                  @Query("per_page") perPage: Int): Single<List<Contributor>>
    //Help wanted! We can load user's bio from this api. But this api can only load one user.
    @GET("/users/{username}")
    fun getUserRepos(@Path("username") user: String): Single<Contributor>
}
