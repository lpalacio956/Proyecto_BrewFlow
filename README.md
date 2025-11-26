# ☕ BrewFlow – Guía Inteligente para Preparar Café

BrewFlow es una aplicación móvil desarrollada en **Kotlin/Android**, diseñada para guiar a usuarios aficionados y baristas principiantes en la correcta preparación del café.  
Incluye temporizador paso a paso, sugerencias según tipo de grano, cálculo de proporciones y un modo avanzado para usuarios con conocimiento profesional.

---

## 📌 Índice

1. [Descripción general](#descripción-general)  
2. [Características](#características)  
3. [Arquitectura del sistema](#arquitectura-del-sistema)  
4. [Requisitos](#requisitos)  
5. [Instalación](#instalación)  
6. [Manual técnico](#manual-técnico)  
7. [Casos de uso y UML](#casos-de-uso-y-uml)  
8. [Backlog y metodología ágil](#backlog-y-metodología-ágil)  
9. [Roadmap](#roadmap)  
10. [Tecnologías utilizadas](#tecnologías-utilizadas)  
11. [Capturas y prototipos](#capturas-y-prototipos)  
12. [Contribuciones](#contribuciones)  
13. [Licencia](#licencia)

---

# 📘 Descripción general

**BrewFlow** es una app educativa para guiar la preparación de café mediante:

- Métodos filtrados (V60, Prensa Francesa, etc.)
- Guía paso a paso con temporizador
- Barra de progreso
- Sugerencias según grano
- Cálculo automático de proporciones café/agua
- Modo barista con recomendaciones avanzadas
- Integración futura con API para actualizar parámetros

Este proyecto fue desarrollado bajo una estrategia ágil, usando análisis UML y arquitectura MVVM.

---

# 🚀 Características

## 🟦 Fase 1 – MVP (Completado)
- Seleccionar método de preparación  
- Iniciar guía paso a paso  
- Temporizador  
- Barra de progreso  
- Sugerencias básicas por grano  
- Calculadora de proporciones  

## 🟩 Fase 2 – Funcionalidades avanzadas (Completado)
- Modo barista  
- Recomendaciones avanzadas (temperatura, molienda, extracción)  
- Ajustes profesionales de proporciones  
- Preferencias de sabor  
- Interfaz optimizada (UI/UX)  
- Conexión con API externa  

---

# 🏗️ Arquitectura del sistema

El proyecto está construido en **Kotlin** utilizando el patrón **MVVM**.

