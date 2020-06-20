package ar.edu.unq.pdes.myprivateblog.di

import android.content.Context
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.myprivateblog.BaseApplication
import ar.edu.unq.pdes.myprivateblog.MainActivity
import ar.edu.unq.pdes.myprivateblog.MainActivityViewModel
import ar.edu.unq.pdes.myprivateblog.data.AppDatabase
import ar.edu.unq.pdes.myprivateblog.data.BlogEntriesRemoteRepository
import ar.edu.unq.pdes.myprivateblog.data.BlogEntriesRepository
import ar.edu.unq.pdes.myprivateblog.screens.login.LoginFragment
import ar.edu.unq.pdes.myprivateblog.screens.login.LoginViewModel
import ar.edu.unq.pdes.myprivateblog.screens.post_create.PostCreateFragment
import ar.edu.unq.pdes.myprivateblog.screens.post_create.PostCreateViewModel
import ar.edu.unq.pdes.myprivateblog.screens.post_detail.PostDetailFragment
import ar.edu.unq.pdes.myprivateblog.screens.post_detail.PostDetailViewModel
import ar.edu.unq.pdes.myprivateblog.screens.post_edit.PostEditFragment
import ar.edu.unq.pdes.myprivateblog.screens.post_edit.PostEditViewModel
import ar.edu.unq.pdes.myprivateblog.screens.posts_listing.PostsListingFragment
import ar.edu.unq.pdes.myprivateblog.screens.posts_listing.PostsListingViewModel
import ar.edu.unq.pdes.myprivateblog.services.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.*
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        MainActivityModule::class,
        LoggerModule::class,
        AuthModule::class,
        EncryptionModule:: class
    ]
)
interface ApplicationComponent : AndroidInjector<BaseApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}

@Module
open class ApplicationModule {

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        return AppDatabase.generateDatabase(context)
    }

    @Singleton
    @Provides
    fun provideBlogEntriesRemoteRepository(authService: AuthService): BlogEntriesRemoteRepository {
        return BlogEntriesRemoteRepository(Firebase.firestore, authService)
    }

    @Singleton
    @Provides
    fun provideBlogEntriesRepository(
        appDatabase: AppDatabase,
        remoteRepository: BlogEntriesRemoteRepository
    ): BlogEntriesRepository {
        return BlogEntriesRepository(appDatabase.blogEntriesDao(), remoteRepository)
    }
}


@Module(
    includes = [
        PostsListingModule::class,
        PostDetailModule::class,
        PostEditModule::class,
        PostCreateModule::class,
        LoginModule::class
    ]
)
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun mainActivity(): MainActivity

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindViewModel(viewmodel: MainActivityViewModel): ViewModel
}

@Module
abstract class PostsListingModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun postsListingFragment(): PostsListingFragment

    @Binds
    @IntoMap
    @ViewModelKey(PostsListingViewModel::class)
    abstract fun bindViewModel(viewmodel: PostsListingViewModel): ViewModel
}

@Module
abstract class PostDetailModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun postDetailFragment(): PostDetailFragment

    @Binds
    @IntoMap
    @ViewModelKey(PostDetailViewModel::class)
    abstract fun bindViewModel(viewmodel: PostDetailViewModel): ViewModel
}

@Module
abstract class PostEditModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun postEditFragment(): PostEditFragment

    @Binds
    @IntoMap
    @ViewModelKey(PostEditViewModel::class)
    abstract fun bindViewModel(viewmodel: PostEditViewModel): ViewModel
}

@Module
abstract class PostCreateModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun postCreateFragment(): PostCreateFragment

    @Binds
    @IntoMap
    @ViewModelKey(PostCreateViewModel::class)
    abstract fun bindViewModel(viewmodel: PostCreateViewModel): ViewModel
}

@Module
open class LoggerModule {

    @Singleton
    @Provides
    fun provideAnaliticsLogger(context: Context): AnalyticsService {
        return FirebaseAnalytics(context)
    }
}

@Module
open class AuthModule {

    @Singleton
    @Provides
    open fun provideAuthService(context: Context): AuthService {
        return FirebaseAuthService(context)
    }
}

@Module
abstract class LoginModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun loginFragment(): LoginFragment

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindViewModel(viewmodel: LoginViewModel): ViewModel
}
@Module
open class EncryptionModule {

    @Singleton
    @Provides
    open fun provideEncryptionService(context: Context): EncryptionService{
        return EncryptionService(context)
    }
}