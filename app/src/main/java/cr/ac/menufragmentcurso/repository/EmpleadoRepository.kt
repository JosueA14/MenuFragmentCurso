package cr.ac.menufragmentcurso.repository

import cr.ac.menufragmentcurso.entity.Empleado
import cr.ac.menufragmentcurso.service.EmpleadoService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EmpleadoRepository {

    val empleadoService : EmpleadoService

    companion object {
        @JvmStatic
        val instance by lazy {
            EmpleadoRepository().apply {  }
        }
    }

    constructor(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://etiquicia.click/restAPI/api.php/records/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        empleadoService = retrofit.create(EmpleadoService::class.java)
    }
    fun save(empleado: Empleado){
        empleadoService.save(empleado).execute()
    }
    fun edit(empleado: Empleado){
        empleadoService.update(empleado.idEmpleado, empleado).execute()
    }
    fun delete(empleado: Empleado){
        empleadoService.delete(empleado.idEmpleado).execute()
    }
    fun datos (): List<Empleado>{
        return empleadoService.getEmpleado().execute().body()!!.records
    }
}