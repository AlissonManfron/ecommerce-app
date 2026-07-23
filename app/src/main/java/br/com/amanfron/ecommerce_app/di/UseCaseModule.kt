package br.com.amanfron.ecommerce_app.di

import br.com.amanfron.ecommerce_app.core.domain.usecase.AddProductToCartUseCase
import br.com.amanfron.ecommerce_app.core.domain.usecase.DeleteCartItemUseCase
import br.com.amanfron.ecommerce_app.core.domain.usecase.GetCartItemsUseCase
import br.com.amanfron.ecommerce_app.core.domain.usecase.GetProductDetailUseCase
import br.com.amanfron.ecommerce_app.core.domain.usecase.GetProductsCountUseCase
import br.com.amanfron.ecommerce_app.core.domain.usecase.GetRankedProductsUseCase
import br.com.amanfron.ecommerce_app.core.domain.usecase.LoginUseCase
import br.com.amanfron.ecommerce_app.core.repository.AuthRepository
import br.com.amanfron.ecommerce_app.core.repository.ProductRepository
import br.com.amanfron.ecommerce_app.core.repository.ShoppingCartRepository
import br.com.amanfron.ecommerce_app.core.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetRankedProductsUseCase(repository: ProductRepository): GetRankedProductsUseCase {
        return GetRankedProductsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetProductDetailUseCase(repository: ProductRepository): GetProductDetailUseCase {
        return GetProductDetailUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ): LoginUseCase {
        return LoginUseCase(authRepository, userRepository)
    }

    @Provides
    @Singleton
    fun provideAddProductToCartUseCase(repository: ShoppingCartRepository): AddProductToCartUseCase {
        return AddProductToCartUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetCartItemsUseCase(repository: ShoppingCartRepository): GetCartItemsUseCase {
        return GetCartItemsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetProductsCountUseCase(repository: ShoppingCartRepository): GetProductsCountUseCase {
        return GetProductsCountUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteCartItemUseCase(repository: ShoppingCartRepository): DeleteCartItemUseCase {
        return DeleteCartItemUseCase(repository)
    }
}
