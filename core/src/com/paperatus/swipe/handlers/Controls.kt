package com.paperatus.swipe.handlers

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.paperatus.swipe.objects.GameObject
import ktx.box2d.body
import ktx.log.info

interface Component {
    enum class Order {
        PRE_UPDATE, UPDATE, POST_UPDATE,
        PRE_RENDER, RENDER, POST_RENDER,
        MANUAL
    }

    val order: Order

    fun update(character: GameObject)
}

/**
 * The Input component of the Player class.
 */
abstract class InputComponent : Component {
    override val order = Component.Order.PRE_UPDATE
}

/**
 * Provides touch-event based input to control the Player
 */
class PlayerTouchInput : InputComponent() {
    override fun update(character: GameObject) {

    }
}

abstract class PhysicsComponent : Component {
    override val order = Component.Order.PRE_UPDATE

    abstract fun initBody(world: World)
}

class PlayerPhysicsComponent : PhysicsComponent() {
    var body: Body? = null
    val radius = 0.7f

    override fun initBody(world: World) {
        body = world.body(BodyDef.BodyType.DynamicBody) {
            // TODO: Dispose created shape
            circle(radius)
        }
    }

    override fun update(character: GameObject) {
        character.position.set(
                body!!.position.x - character.bounds.width / 2.0f,
                body!!.position.y - character.bounds.height / 2.0f
        )
    }

}