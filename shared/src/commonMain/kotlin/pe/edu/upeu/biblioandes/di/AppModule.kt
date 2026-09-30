package pe.edu.upeu.biblioandes.di

import org.koin.core.context.startKoin
import org.koin.dsl.module
import pe.edu.upeu.biblioandes.data.local.DatosSimulados
import pe.edu.upeu.biblioandes.data.repository.BibliotecaRepositoryFake
import pe.edu.upeu.biblioandes.domain.repository.BibliotecaRepository
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerCatalogoUseCase
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerDetalleLibroUseCase
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerEstudianteUseCase
import pe.edu.upeu.biblioandes.domain.usecase.ObtenerPrestamosUseCase
import pe.edu.upeu.biblioandes.domain.usecase.SolicitarPrestamoUseCase
import pe.edu.upeu.biblioandes.presentation.catalogo.CatalogoViewModel
import pe.edu.upeu.biblioandes.presentation.detalle.DetalleLibroViewModel
import pe.edu.upeu.biblioandes.presentation.inicio.InicioViewModel
import pe.edu.upeu.biblioandes.presentation.perfil.PerfilViewModel
import pe.edu.upeu.biblioandes.presentation.prestamos.PrestamosViewModel
import pe.edu.upeu.biblioandes.presentation.theme.TemaViewModel

val appModule = module {
    single<BibliotecaRepository> { BibliotecaRepositoryFake() }
    factory { ObtenerCatalogoUseCase(get()) }
    factory { ObtenerDetalleLibroUseCase(get()) }
    factory { ObtenerEstudianteUseCase(get()) }
    factory { ObtenerPrestamosUseCase(get()) }
    factory { SolicitarPrestamoUseCase(get()) }
    factory { InicioViewModel(get(), get(), DatosSimulados.estudiante.codigo) }
    factory { CatalogoViewModel(get()) }
    factory { DetalleLibroViewModel(get(), get(), DatosSimulados.estudiante.codigo) }
    factory { PrestamosViewModel(get(), DatosSimulados.estudiante.codigo) }
    factory { PerfilViewModel(get()) }
    single { TemaViewModel() }
}

fun initKoin() {
    startKoin { modules(appModule) }
}
