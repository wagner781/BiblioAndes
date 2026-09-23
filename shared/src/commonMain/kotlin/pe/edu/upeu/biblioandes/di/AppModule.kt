package pe.edu.upeu.biblioandes.di

import org.koin.dsl.module
import org.koin.core.module.dsl.viewModelOf
import pe.edu.upeu.biblioandes.data.repository.BibliotecaRepositoryFake
import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerCatalogoUseCase
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerPrestamosUseCase
import pe.edu.upeu.biblioandes.domain.usecase.SolicitarPrestamoUseCase
import pe.edu.upeu.biblioandes.ui.catalogo.CatalogoViewModel
import pe.edu.upeu.biblioandes.ui.detalle.DetalleViewModel
import pe.edu.upeu.biblioandes.ui.prestamos.PrestamosViewModel

val appModule = module {
    single<BibliotecaRepository> { BibliotecaRepositoryFake() }
    
    factory { ObtenerCatalogoUseCase(get()) }
    factory { ObtenerPrestamosUseCase(get()) }
    factory { SolicitarPrestamoUseCase(get()) }
    
    viewModelOf(::CatalogoViewModel)
    viewModelOf(::DetalleViewModel)
    viewModelOf(::PrestamosViewModel)
}
