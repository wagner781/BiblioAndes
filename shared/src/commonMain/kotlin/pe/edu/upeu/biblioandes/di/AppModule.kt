package pe.edu.upeu.biblioandes.di

import org.koin.dsl.module
import pe.edu.upeu.biblioandes.data.repository.BibliotecaRepositoryFake
import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerCatalogoUseCase
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerPrestamosUseCase
import pe.edu.upeu.biblioandes.domain.usecase.SolicitarPrestamoUseCase

val appModule = module {
    single<BibliotecaRepository> { BibliotecaRepositoryFake() }
    
    factory { ObtenerCatalogoUseCase(get()) }
    factory { ObtenerPrestamosUseCase(get()) }
    factory { SolicitarPrestamoUseCase(get()) }
}
