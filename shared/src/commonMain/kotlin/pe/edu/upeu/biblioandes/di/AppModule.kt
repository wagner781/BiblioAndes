package pe.edu.upeu.biblioandes.di

import org.koin.core.context.startKoin
import org.koin.dsl.module
import pe.edu.upeu.biblioandes.data.repository.BibliotecaRepositoryFake
import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerCatalogoUseCase
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerPrestamosUseCase
import pe.edu.upeu.biblioandes.domain.usecase.SolicitarPrestamoUseCase
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerEstudianteUseCase
import pe.edu.upeu.biblioandes.presentation.catalogo.CatalogoViewModel
import pe.edu.upeu.biblioandes.presentation.detalle.DetalleLibroViewModel
import pe.edu.upeu.biblioandes.presentation.prestamos.PrestamosViewModel
import pe.edu.upeu.biblioandes.presentation.inicio.InicioViewModel

val appModule = module {
    single<BibliotecaRepository> { BibliotecaRepositoryFake() }
    
    factory { ObtenerCatalogoUseCase(get()) }
    factory { ObtenerPrestamosUseCase(get()) }
    factory { SolicitarPrestamoUseCase(get(), get()) }
    factory { ObtenerEstudianteUseCase(get()) }
    
    factory { CatalogoViewModel(get()) }
    factory { DetalleLibroViewModel(get()) }
    factory { PrestamosViewModel(get()) }
    factory { InicioViewModel(get(), get()) }
}

fun initKoin() {
    startKoin {
        modules(appModule)
    }
}
