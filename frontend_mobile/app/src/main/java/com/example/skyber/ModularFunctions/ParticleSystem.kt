package com.example.skyber.ModularFunctions

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

data class Particle(
    var x: Float,
    var y: Float,
    var radius: Float,
    var speed: Float,
    var directionX: Float,
    var directionY: Float,
    var color: Color,
    var connections: MutableList<Int> = mutableListOf()
)

@Composable
fun ParticleSystem(
    modifier: Modifier = Modifier,
    particleColor: Color = Color.White,
    particleCount: Int = 40,
    maxRadius: Float = 3f,
    minRadius: Float = 1f,
    maxSpeed: Float = 1f,
    lineColor: Color = Color.White.copy(alpha = 0.3f),
    lineThickness: Float = 1f,
    connectionDistance: Float = 150f,
    interactiveRadius: Float = 200f,
    backgroundColor: Color = Color(0xFF0D47A1) // Blue background similar to your web design
) {
    val particles = remember {
        List(particleCount) {
            Particle(
                x = Random.nextFloat() * 1000,
                y = Random.nextFloat() * 2000,
                radius = Random.nextFloat() * (maxRadius - minRadius) + minRadius,
                speed = Random.nextFloat() * maxSpeed,
                directionX = if (Random.nextBoolean()) 1f else -1f,
                directionY = if (Random.nextBoolean()) 1f else -1f,
                color = particleColor.copy(alpha = 0.5f + Random.nextFloat() * 0.5f)
            )
        }.toMutableStateList()
    }

    var canvasWidth by remember { mutableStateOf(0f) }
    var canvasHeight by remember { mutableStateOf(0f) }
    var touchPosition by remember { mutableStateOf<Offset?>(null) }

    val animatedProgress = rememberInfiniteTransition(label = "particles")
    val progress by animatedProgress.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particles animation"
    )

    LaunchedEffect(progress) {
        particles.forEachIndexed { index, particle ->
            // Move particles
            particle.x += particle.directionX * particle.speed
            particle.y += particle.directionY * particle.speed

            // Bounce off edges
            if (particle.x < 0) {
                particle.directionX = 1f
            } else if (particle.x > canvasWidth) {
                particle.directionX = -1f
            }

            if (particle.y < 0) {
                particle.directionY = 1f
            } else if (particle.y > canvasHeight) {
                particle.directionY = -1f
            }

            // Reset connections
            particle.connections.clear()

            // Check connections with other particles
            for (i in particles.indices) {
                if (i != index) {
                    val dx = particle.x - particles[i].x
                    val dy = particle.y - particles[i].y
                    val distance = sqrt(dx.pow(2) + dy.pow(2))

                    if (distance < connectionDistance) {
                        particle.connections.add(i)
                    }
                }
            }

            // Interactive behavior with touch
            touchPosition?.let { pos ->
                val dx = particle.x - pos.x
                val dy = particle.y - pos.y
                val distance = sqrt(dx.pow(2) + dy.pow(2))

                if (distance < interactiveRadius) {
                    val force = (interactiveRadius - distance) / interactiveRadius
                    val dirX = dx / distance
                    val dirY = dy / distance

                    particle.x += dirX * force * 2
                    particle.y += dirY * force * 2
                }
            }
        }
    }

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { touchPosition = it },
                    onDrag = { change, _ -> touchPosition = change.position },
                    onDragEnd = { touchPosition = null },
                    onDragCancel = { touchPosition = null }
                )
            }
    ) {
        canvasWidth = size.width
        canvasHeight = size.height

        // Draw background
        drawRect(backgroundColor)

        // Draw connections
        particles.forEachIndexed { index, particle ->
            particle.connections.forEach { connectedIndex ->
                val connectedParticle = particles[connectedIndex]
                val startPoint = Offset(particle.x, particle.y)
                val endPoint = Offset(connectedParticle.x, connectedParticle.y)

                val dx = particle.x - connectedParticle.x
                val dy = particle.y - connectedParticle.y
                val distance = sqrt(dx.pow(2) + dy.pow(2))

                // Line opacity based on distance
                val opacity = (1 - distance / connectionDistance) * 0.8f

                drawLine(
                    color = lineColor.copy(alpha = opacity),
                    start = startPoint,
                    end = endPoint,
                    strokeWidth = lineThickness
                )
            }
        }

        // Draw particles
        particles.forEach { particle ->
            drawCircle(
                color = particle.color,
                radius = particle.radius,
                center = Offset(particle.x, particle.y)
            )
        }
    }
}