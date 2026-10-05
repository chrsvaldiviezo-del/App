package com.example.data

object NewsRepository {

    fun getInitialNews(): List<NewsArticle> {
        val now = System.currentTimeMillis()
        return listOf(
            NewsArticle(
                id = "news_1",
                title = "BCV realiza nueva intervención cambiaria en la banca nacional para contener el tipo de cambio",
                summary = "El Banco Central de Venezuela inyectó 85 millones de dólares a las mesas de cambio bancarias para estabilizar la cotización oficial de la divisa frente al avance de la demanda de liquidez.",
                fullContent = """
                    El Banco Central de Venezuela (BCV) ejecutó este lunes su cuadragésima intervención cambiaria del año, colocando un monto estimado de 85 millones de dólares estadounidenses en las mesas de cambio del sistema financiero nacional.

                    Con esta inyección directa de divisas en efectivo y transferencias interbancarias, la autoridad monetaria procura contener el avance del tipo de cambio oficial frente al bolívar y mitigar la brecha cambiaria existente con los mercados no regulados. Fuentes vinculadas a la tesorería de las principales entidades bancarias públicas y privadas confirmaron que las posturas fueron adjudicadas a la tasa oficial ponderada fijada en la jornada previa.

                    Analistas económicos destacan que la política de intervención semanal ha sido el instrumento prioritario del emisor para anclar las expectativas inflacionarias, requiriendo un monitoreo constante del flujo de ingresos petroleros y la disciplina de gasto público. Los comercios y prestadores de servicios a nivel nacional están legalmente obligados a emplear exclusivamente la tasa resultante de estas mesas de cambio bancarias publicadas por el BCV.
                """.trimIndent(),
                category = NewsCategory.ECONOMY,
                status = VerificationStatus.VERIFIED,
                source = "Banco Central de Venezuela (BCV)",
                timeAgo = "Hace 22 min",
                verificationNotice = "Oficialmente verificado: Datos cotejados con el reporte oficial de operaciones cambiarias del BCV y circulares bancarias.",
                factCheckNotes = "Información confirmada por portales financieros acreditados y las cotizaciones emitidas por las entidades bancarias participantes.",
                isBreaking = true,
                likesCount = 482,
                readsCount = "8.9k",
                publishedAtMillis = now - 22 * 60 * 1000L,
                sourceUrl = "https://www.bcv.org.ve/"
            ),
            NewsArticle(
                id = "news_2",
                title = "⚠️ RUMOR EN REDES: Supuesto lanzamiento inminente de nuevos billetes de 500 y 1.000 Bolívares",
                summary = "Circulan en canales de Telegram y X supuestas imágenes de nuevas denominaciones del cono monetario que presuntamente comenzarían a circular este mes.",
                fullContent = """
                    Durante las últimas 48 horas se ha viralizado en redes sociales (principalmente X, TikTok y cadenas de mensajería instantánea) una serie de imágenes que muestran presuntos billetes de 500 y 1.000 bolívares, acompañados de mensajes que afirman su inminente distribución por parte de la banca pública.

                    No obstante, tras la verificación técnica y consulta a los canales oficiales del Banco Central de Venezuela (BCV) y el Ministerio del Poder Popular de Economía y Finanzas, se confirma que NO existe ninguna resolución, decreto ni providencia administrativa en Gaceta Oficial que ordene la ampliación del cono monetario vigente.

                    Especialistas en diseño numismático identificaron que las imágenes corresponden a montajes digitales que reciclan patrones gráficos de conos monetarios anteriores (como el Bolívar Soberano de 2018). Se exhorta a la ciudadanía a no replicar cadenas no oficiales que busquen generar desinformación o especulación en el comercio diario.
                """.trimIndent(),
                category = NewsCategory.ECONOMY,
                status = VerificationStatus.RUMOR,
                source = "Tendencia viral en X / Telegram",
                timeAgo = "Hace 45 min",
                verificationNotice = "ALERTA DE RUMOR: Contenido no verificado. Ninguna institución oficial ha emitido comunicados sobre nueva emisión de billetes.",
                factCheckNotes = "Desmentido por analistas numismáticos. La Casa de la Moneda y el BCV no registran solicitudes de impresión pública para estas denominaciones.",
                isBreaking = false,
                likesCount = 215,
                readsCount = "14.2k",
                publishedAtMillis = now - 45 * 60 * 1000L,
                sourceUrl = "https://www.bcv.org.ve/politica-monetaria/billetes-y-monedas"
            ),
            NewsArticle(
                id = "news_3",
                title = "Acuerdo bilateral entre Venezuela y Colombia reactiva 6 nuevas frecuencias de vuelos semanales",
                summary = "Las autoridades aeronáuticas de ambos países autorizaron el incremento de vuelos directos entre Caracas, Bogotá y Medellín para dinamizar el intercambio comercial.",
                fullContent = """
                    El Instituto Nacional de Aeronáutica Civil (INAC) de Venezuela y la Unidad Administrativa Especial de Aeronáutica Civil de Colombia oficializaron la ampliación del cuadro de rutas bilaterales, autorizando seis nuevas frecuencias de vuelos regulares semanales entre Caracas, Bogotá y Medellín.

                    El acuerdo permite a las aerolíneas Laser Airlines, Avianca, Wingo y Latam incrementar sus itinerarios de manera escalonada a partir de la próxima semana. La medida busca responder al incremento en la demanda de pasajeros corporativos y turísticos, así como reducir las tarifas medias de los boletos aéreos al fomentar una mayor oferta de asientos.

                    Asimismo, las mesas técnicas binacionales acordaron avanzar en la homologación de permisos fitosanitarios y de aduanas para optimizar el transporte de carga aérea perecedera entre ambos países, consolidando la reactivación comercial fronteriza.
                """.trimIndent(),
                category = NewsCategory.NATIONAL,
                status = VerificationStatus.VERIFIED,
                source = "INAC / Aerocivil Colombia",
                timeAgo = "Hace 1 hora",
                verificationNotice = "Confirmado oficial: Notificado mediante NOTAM de aviación civil y comunicados de las aerolíneas involucradas.",
                factCheckNotes = "Corroborado con los itinerarios publicados en los sistemas de reserva de las aerolíneas autorizadas.",
                isBreaking = false,
                likesCount = 340,
                readsCount = "5.1k",
                publishedAtMillis = now - 60 * 60 * 1000L,
                sourceUrl = "https://www.inac.gob.ve/"
            ),
            NewsArticle(
                id = "news_4",
                title = "EN DESARROLLO: Negociaciones sobre licencias energéticas en Washington entran en fase técnica",
                summary = "Fuentes diplomáticas señalan revisión de términos operativos para exportación de crudo y gas hacia refinerías del Caribe y EE.UU.",
                fullContent = """
                    Equipos técnicos del Departamento del Tesoro de EE.UU. (a través de la Oficina de Control de Activos Extranjeros, OFAC) y representantes de empresas energéticas multinacionales como Repsol, Chevron y Maurel & Prom sostienen reuniones para evaluar las condiciones de las licencias particulares emitidas para operar en yacimientos venezolanos.

                    Las deliberaciones se concentran en establecer mecanismos transparentes de auditoría financiera que aseguren el pago de regalías e impuestos al Estado venezolano, permitiendo al mismo tiempo que las petroleras reinviertan en el mantenimiento de pozos, compresión de gas y seguridad ambiental en la Faja Petrolífera del Orinoco y el Lago de Maracaibo.

                    Aunque aún no se ha divulgado el texto de un comunicado final conjunto, voceros del sector petrolero privado califican el diálogo como constructivo para garantizar la estabilidad del suministro energético regional.
                """.trimIndent(),
                category = NewsCategory.POLITICS,
                status = VerificationStatus.DEVELOPING,
                source = "Corresponsalías Internacionales / Reuters",
                timeAgo = "Hace 2 horas",
                verificationNotice = "En desarrollo: Fuentes de primera línea en Washington y Caracas reportan reuniones en curso sin declaración final conjunta.",
                factCheckNotes = "Información contrastada con voceros del sector petrolero privado; los detalles específicos de las cláusulas se mantienen en reserva.",
                isBreaking = false,
                likesCount = 189,
                readsCount = "3.8k",
                publishedAtMillis = now - 2 * 60 * 60 * 1000L,
                sourceUrl = "https://home.treasury.gov/policy-issues/financial-sanctions/sanctions-programs-and-country-information/venezuela-related-sanctions"
            ),
            NewsArticle(
                id = "news_5",
                title = "⚠️ RUMOR EN REDES: Cadena viral afirma que puntos de venta suspenderán cobros en divisas este fin de semana",
                summary = "Mensaje difundido masivamente en WhatsApp asegura falsamente que los comercios no podrán pasar tarjetas internacionales por mantenimiento bancario prolongado.",
                fullContent = """
                    Un mensaje en formato de cadena difundido masivamente en grupos vecinales de WhatsApp ha generado zozobra, advirtiendo sobre una supuesta suspensión generalizada de los puntos de venta (POS) y de los pagos con tarjetas internacionales Visa/MasterCard durante 72 horas por 'adecuaciones del sistema IGTF'.

                    La Asociación Bancaria de Venezuela (ABV) y el consorcio Suiche 7B desmintieron categóricamente esta versión, ratificando que toda la infraestructura de pagos electrónicos, cajeros automáticos, pasarelas de pago web y puntos de venta en comercios opera con total normalidad las 24 horas del día.

                    La ABV aclaró que los procesos rutinarios de mantenimiento preventivo se ejecutan exclusivamente en horas de la madrugada (entre 2:00 a.m. y 4:00 a.m.) en ventanas cortas que no interrumpen el normal desenvolvimiento de las transacciones comerciales.
                """.trimIndent(),
                category = NewsCategory.SERVICES,
                status = VerificationStatus.RUMOR,
                source = "Cadena masiva en WhatsApp",
                timeAgo = "Hace 3 horas",
                verificationNotice = "DESMENTIDO / RUMOR FALSO: La Asociación Bancaria de Venezuela ratificó la operatividad ininterrumpida de los puntos de venta.",
                factCheckNotes = "No comparta esta cadena. Se trata de un bulo recurrente que reaparece periódicamente en redes sociales cambiando las fechas.",
                isBreaking = false,
                likesCount = 560,
                readsCount = "22.5k",
                publishedAtMillis = now - 3 * 60 * 60 * 1000L,
                sourceUrl = "https://asociacionbancaria.com/"
            ),
            NewsArticle(
                id = "news_6",
                title = "Sudeban actualiza límites diarios para transferencias inmediatas e interbancarias Pago Móvil",
                summary = "Entidades bancarias adecúan montos máximos permitidos en plataformas P2P y P2C para personas naturales y comercios, mejorando la agilidad en pagos cotidianos.",
                fullContent = """
                    La Superintendencia de las Instituciones del Sector Bancario (Sudeban) instruyó a todas las instituciones de la banca pública y privada a elevar los límites operativos en sus plataformas de Pago Móvil interbancario para personas naturales (P2P), de persona a comercio (P2C) y de comercio a persona (C2P).

                    Bancos líderes como Banco de Venezuela, Mercantil, Banesco y BBVA Provincial ya han adecuado sus sistemas informáticos, permitiendo a sus clientes transferir montos sustancialmente mayores por operación y acumulados diarios.

                    Esta actualización responde a la necesidad de acompañar la dinámica del comercio nacional en bolívares, facilitando que los usuarios cancelen compras de bienes de consumo masivo, servicios médicos y gastos familiares sin experimentar rebotes por exceder topes diarios.
                """.trimIndent(),
                category = NewsCategory.ECONOMY,
                status = VerificationStatus.VERIFIED,
                source = "Sudeban Circular Institucional",
                timeAgo = "Hace 4 horas",
                verificationNotice = "Oficial: Circular Sudeban publicada y aplicada en la plataforma web de los principales bancos venezolanos.",
                factCheckNotes = "Usuarios reportan que los nuevos límites ya se encuentran disponibles en las aplicaciones móviles bancarias.",
                isBreaking = false,
                likesCount = 612,
                readsCount = "11.4k",
                publishedAtMillis = now - 4 * 60 * 60 * 1000L,
                sourceUrl = "https://www.sudeban.gob.ve/"
            ),
            NewsArticle(
                id = "news_7",
                title = "Precios del crudo marcador Brent y Cesta Venezolana cierran la semana con ligera alza",
                summary = "La cotización del barril de petróleo sube impulsada por tensiones geopolíticas en Medio Oriente y sostenida demanda asiática.",
                fullContent = """
                    Los principales marcadores internacionales de hidrocarburos registraron alzas al cierre de la sesión de los mercados de materias primas. El barril de crudo Brent del Mar del Norte para entrega a término avanzó un 1,4% ubicándose en 78,60 dólares por barril, mientras que el West Texas Intermediate (WTI) superó los 74 dólares.

                    En sintonía con este comportamiento, la Cesta Petrolera Venezolana (referenciada principalmente por el crudo Merey 16 de la Faja del Orinoco) promedió los 64,15 dólares por barril, lo que representa un alivio moderado para el flujo de caja en divisas del Estado.

                    El informe mensual de la OPEP estima que la producción nacional de crudo en Venezuela se mantuvo estable alrededor de los 900.000 barriles diarios según fuentes secundarias, impulsada por las operaciones conjuntas en el occidente y oriente del país.
                """.trimIndent(),
                category = NewsCategory.INTERNATIONAL,
                status = VerificationStatus.VERIFIED,
                source = "Reportes OPEP / Mercados de Commodities",
                timeAgo = "Hace 5 horas",
                verificationNotice = "Verificado: Datos oficiales de los mercados bursátiles y el informe mensual de la secretaría de la OPEP.",
                factCheckNotes = "Las cifras coinciden con las pizarras de cotizaciones de Bloomberg Energy y Platts.",
                isBreaking = false,
                likesCount = 145,
                readsCount = "2.9k",
                publishedAtMillis = now - 5 * 60 * 60 * 1000L,
                sourceUrl = "https://www.opec.org/"
            ),
            NewsArticle(
                id = "news_8",
                title = "EN DESARROLLO: Trabajos de mantenimiento en línea de transmisión eléctrica de los Valles del Tuy",
                summary = "Corpoelec ejecuta maniobras preventivas que causan bajas de tensión parciales en sectores de Miranda y Distrito Capital.",
                fullContent = """
                    Cuadrillas técnicas de la Corporación Eléctrica Nacional (Corpoelec) iniciaron labores de sustitución de aislamiento, poda preventiva y calibración de transformadores en la subestación de alta tensión Santa Teresa, ubicada en los Valles del Tuy, estado Miranda.

                    Vecinos de sectores como Charallave, Cúa, Ocumare del Tuy y zonas del sureste de Caracas reportaron fluctuaciones temporales de voltaje durante las primeras horas de la mañana.

                    Voceros de la estatal eléctrica señalaron que las labores forman parte del Plan de Mantenimiento del Sistema Eléctrico Nacional (SEN) para reducir contingencias durante la temporada de lluvias, previéndose la normalización plena de los circuitos al concluir la jornada de trabajo vespertina.
                """.trimIndent(),
                category = NewsCategory.SERVICES,
                status = VerificationStatus.DEVELOPING,
                source = "Reportes vecinales y cuadrillas Corpoelec",
                timeAgo = "Hace 6 horas",
                verificationNotice = "En desarrollo: Trabajos reportados en progreso por autoridades regionales y usuarios en tiempo real.",
                factCheckNotes = "El suministro eléctrico se restablece progresivamente en los circuitos alternos de distribución.",
                isBreaking = false,
                likesCount = 98,
                readsCount = "4.2k",
                publishedAtMillis = now - 6 * 60 * 60 * 1000L,
                sourceUrl = "https://corpoelec.gob.ve/"
            )
        )
    }
}
