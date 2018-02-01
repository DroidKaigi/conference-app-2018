package io.github.droidkaigi.confsched2018.data.api

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface SessionFeedbackApi {

    @POST("e/1FAIpQLSdNeo0djZbTRb1toK4mIRiVcgT4Gif6YZCk5b3WYEl-c77d5w/formResponse")
    @FormUrlEncoded
    fun submitSessionFeedback(

            @Field("entry.1298546024") sessionId: String,
            @Field("entry.413792998") sessionTitle: String,

            // セッションはどうでしたか？(総合評価)
            @Field("entry.1141848398") totalEvaluation: Int,
            // セッションはあなたの仕事やプロジェクトに関連がありましたか?
            @Field("entry.335146475") relevancy: Int,
            // セッションの説明を読んで期待した通りの内容でしたか?
            @Field("entry.1916895481") asExpected: Int,
            // セッションの難易度はいかがでしたか?
            @Field("entry.1501292277") difficulty: Int,
            // 技術的な知見は得られたでしょうか?
            @Field("entry.2121897737") knowledgeable: Int,

            @Field("entry.645604473") comment: String): Single<Response<Void>>
}
