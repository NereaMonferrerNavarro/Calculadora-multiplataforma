package com.example.calculadoramultiplataforma



import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import calculadoramultiplataforma.shared.generated.resources.Res
import calculadoramultiplataforma.shared.generated.resources.logo
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource


// Paleta de día y noche

data class Paleta(
    val fondoEntrada: Color, val fondoDespedida: Color, val fondoCalc: Color,
    val pantalla: Color, val textoPantalla: Color, val textoOperacion: Color,
    val numero: Color, val numeroP: Color, val funcion: Color, val funcionP: Color,
    val salir: Color, val salirP: Color
)

val PaletaDia = Paleta(
    Color(0xFF6E6BB5), Color(0xFF3F4A8A), Color(0xFFEEEEF5),
    Color(0xFFD6D6E6), Color(0xFF1F1F3D), Color(0xFF1F1F3D),
    Color(0xFF8E91B8), Color(0xFF6D7098), Color(0xFF7C80B8), Color(0xFF5C60A0),
    Color(0xFF6E6BB5), Color(0xFF55539A)
)

val PaletaNoche = Paleta(
    Color(0xFF4F4E7E), Color(0xFF1F1F3D), Color(0xFF303034),
    Color(0xFF59586F), Color(0xFFFFFFFF), Color(0xFFD0D0E0),
    Color(0xFF525470), Color(0xFF7A7DA8), Color(0xFF3D406F), Color(0xFF5C60A0),
    Color(0xFF4F4E7E), Color(0xFF6F6EA6)
)

// Colores fijos normal y pulsado
val Operador = Color(0xFF6353BE) to Color(0xFF8B7BE8)
val BotonAC = Color(0xFF24A4BE) to Color(0xFF4CCBE5)
val BotonIgual = Color(0xFFADA4DE) to Color(0xFFD3CCF5)

fun coloresDe(tecla: String, p: Paleta): Pair<Color, Color> = when (tecla) {
    "√", "π", "^", "!" -> p.funcion to p.funcionP
    "AC" -> BotonAC
    "()", "%", "÷", "×", "−", "+" -> Operador
    "=" -> BotonIgual
    else -> p.numero to p.numeroP
}

// Navegación

enum class Destino { ENTRADA, CALCULADORA, DESPEDIDA }

@Composable
fun App() {
    var oscuro by rememberSaveable { mutableStateOf(true) }
    var destino by rememberSaveable { mutableStateOf(Destino.ENTRADA) }
    val p = if (oscuro) PaletaNoche else PaletaDia
    val alternarTema = { oscuro = !oscuro }

    when (destino) {
        Destino.ENTRADA -> PantallaEntrada(p, { destino = Destino.CALCULADORA }, alternarTema)
        Destino.CALCULADORA -> PantallaCalculadora(p, alternarTema) { destino = Destino.DESPEDIDA }
        Destino.DESPEDIDA -> PantallaDespedida(p) { destino = Destino.ENTRADA }
    }
}

// Boton que cambia el color al pulsar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Boton(
    texto: String,
    normal: Color,
    pulsado: Color,
    modifier: Modifier = Modifier,
    tamano: TextUnit = 24.sp,
    onLongClick: (() -> Unit)? = null,
    onClick: () -> Unit
) {
    val fuente = remember { MutableInteractionSource() }
    val presionado by fuente.collectIsPressedAsState()



    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(if (presionado) pulsado else normal)
            .combinedClickable(
                interactionSource = fuente,
                indication = null,
                onLongClick = onLongClick,
                onClick = {
                    pitido()
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(texto, color = Color.White, fontSize = tamano)
    }
}

// Pantalla de entrada

@Composable
fun PantallaEntrada(p: Paleta, onEntrar: () -> Unit, onTema: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(p.fondoEntrada).safeContentPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(96.dp, 88.dp)
                    .clip(RoundedCornerShape(24.dp)).background(Color(0xFF53B1FF)),
                contentAlignment = Alignment.Center
            ) {
                Image(painter = painterResource(Res.drawable.logo),
                    contentDescription = "Logo de la calculadora")
            }
            Spacer(Modifier.height(20.dp))
            Text("Calculadora", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))
            Text("Creado por: Nerea Monferrer Navarro", color = Color.White, fontSize = 15.sp)
            Spacer(Modifier.height(28.dp))
            Boton("ENTRAR", Operador.first, Operador.second,
                Modifier.size(180.dp, 52.dp), 18.sp, onClick = onEntrar)
            Spacer(Modifier.height(14.dp))
            Boton("🌙/☀️", p.funcion, p.funcionP,
                Modifier.size(180.dp, 44.dp), 15.sp, onClick = onTema)
        }
    }
}

// Esta es la pantalla de despedida de la aplicación

@Composable
fun PantallaDespedida(p: Paleta, onVolver: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(p.fondoDespedida).safeContentPadding(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Saliendo...", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(30.dp))
            Boton("VOLVER AL INICIO", Operador.first, Operador.second,
                Modifier.size(220.dp, 52.dp), 16.sp, onClick = onVolver)
        }
    }
}

// Pantalla de calculo

val Filas = listOf(
    listOf("√", "π", "^", "!"),
    listOf("AC", "()", "%", "÷"),
    listOf("7", "8", "9", "×"),
    listOf("4", "5", "6", "−"),
    listOf("1", "2", "3", "+"),
    listOf("0", ".", "⌫", "=")
)

@Composable
fun PantallaCalculadora(p: Paleta, onTema: () -> Unit, onSalir: () -> Unit) {
    var expresion by rememberSaveable { mutableStateOf("") }
    var verAyuda by rememberSaveable { mutableStateOf(false) }
    var verAviso by rememberSaveable { mutableStateOf(false) }

    fun pulsar(valor: String) {

        when (valor) {
            "AC" -> expresion = ""
            "⌫" -> expresion = expresion.dropLast(1)
            "=" -> verAviso = true
            else -> if (expresion.length < 25) expresion += valor
        }
    }

    val textoArriba = expresion.takeLast(20)
    val textoAbajo = if (expresion.isEmpty()) "0" else expresion.takeLast(12)

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize().background(p.fondoCalc).safeContentPadding()
    ) {
        val horizontal = maxWidth > maxHeight

        if (horizontal) {
            Row(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    Recuadro(textoArriba, p.textoOperacion, p.pantalla, 20.sp, false,
                        Modifier.weight(1f).fillMaxWidth())
                    Spacer(Modifier.height(8.dp))
                    Recuadro(textoAbajo, p.textoPantalla, p.pantalla, 34.sp, true,
                        Modifier.weight(1f).fillMaxWidth())
                    Spacer(Modifier.height(8.dp))
                    FilaInferior(p, onTema, onSalir, { verAyuda = true }, Modifier.height(44.dp))
                }
                Spacer(Modifier.width(8.dp))
                Teclado(p, true, { pulsar(it) }, { verAyuda = true },
                    Modifier.weight(1.7f).fillMaxHeight())
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                Recuadro(textoArriba, p.textoOperacion, p.pantalla, 26.sp, false,
                    Modifier.fillMaxWidth().height(90.dp))
                Spacer(Modifier.height(12.dp))
                Recuadro(textoAbajo, p.textoPantalla, p.pantalla, 48.sp, true,
                    Modifier.fillMaxWidth().height(100.dp))
                Spacer(Modifier.height(10.dp))
                Teclado(p, false, { pulsar(it) }, { verAyuda = true },
                    Modifier.weight(1f).fillMaxWidth())
                Spacer(Modifier.height(6.dp))
                FilaInferior(p, onTema, onSalir, { verAyuda = true }, Modifier.height(44.dp))
            }
        }
    }

    if (verAyuda) {
        AlertDialog(
            onDismissRequest = { verAyuda = false },
            confirmButton = { TextButton(onClick = { verAyuda = false }) { Text("Entendido") } },
            title = { Text("Ayuda") },
            text = {
                Text(
                    "Esta calculadora es una maqueta: no realiza cálculos.\n\n" +
                            "Los números y símbolos se escriben en pantalla.\n\n" +
                            "AC borra todo y ⌫ elimina el último carácter.\n\n" +
                            "El botón TEMA cambia entre modo diurno y nocturno.\n\n" +
                            "SALIR abre la pantalla de despedida.\n\n" +
                            "Mantén pulsada cualquier tecla para ver esta ayuda."
                )
            }
        )
    }

    if (verAviso) {
        AlertDialog(
            onDismissRequest = { verAviso = false },
            confirmButton = { TextButton(onClick = { verAviso = false }) { Text("Vale") } },
            text = { Text("Calculadora no funcional en esta versión") }
        )
    }
}

@Composable
fun Recuadro(
    texto: String, color: Color, fondo: Color, tamano: TextUnit,
    negrita: Boolean, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(fondo).padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            texto, color = color, fontSize = tamano,
            fontWeight = if (negrita) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1, textAlign = TextAlign.End
        )
    }
}

@Composable
fun Teclado(
    p: Paleta, horizontal: Boolean, onTecla: (String) -> Unit,
    onAyuda: () -> Unit, modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Filas.forEachIndexed { indice, fila ->
            val peso = if (indice == 0 && !horizontal) 0.6f else 1f
            Row(modifier = Modifier.weight(peso).fillMaxWidth()) {
                fila.forEach { tecla ->
                    val (normal, pulsado) = coloresDe(tecla, p)
                    val tamano = when {
                        horizontal -> 16.sp
                        indice == 0 -> 20.sp
                        else -> 24.sp
                    }
                    Boton(
                        texto = tecla, normal = normal, pulsado = pulsado,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                            .padding(if (horizontal) 2.dp else 4.dp),
                        tamano = tamano,
                        onLongClick = onAyuda,
                        onClick = { onTecla(tecla) }
                    )
                }
            }
        }
    }
}

@Composable
fun FilaInferior(
    p: Paleta, onTema: () -> Unit, onSalir: () -> Unit,
    onAyuda: () -> Unit, modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Boton("🌙/☀️", p.funcion, p.funcionP, Modifier.weight(1f).fillMaxHeight(), 16.sp, onClick = onTema)
        Spacer(Modifier.width(6.dp))
        Boton("ⓘ", p.funcion, p.funcionP, Modifier.weight(0.6f).fillMaxHeight(), 18.sp, onClick = onAyuda)
        Spacer(Modifier.width(6.dp))
        Boton("SALIR", p.salir, p.salirP, Modifier.weight(1.6f).fillMaxHeight(), 16.sp, onClick = onSalir)
    }
}