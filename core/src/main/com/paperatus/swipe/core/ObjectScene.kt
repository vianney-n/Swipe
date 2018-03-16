package com.paperatus.swipe.core

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.paperatus.swipe.Game
import ktx.collections.GdxArray

/**
 * Contains game components for updating and rendering.
 */
abstract class ObjectScene(protected val game: Game) : Scene {
    val root = GameObject()

    protected val nodeTraversal = NodeTraversal()
    protected open val nodeUpdater = NodeUpdater()
    protected open val nodeRemover = NodeRemover()
    protected open val nodeRenderer = NodeRenderer(game)

    override fun update(delta: Float) {
        nodeTraversal.traverse(nodeUpdater, root, delta)
        nodeTraversal.traverse(nodeRemover, root)
    }

    /**
     * Renders every GameObject in [gameObjects].
     *
     * Calling [render] will retrieve the [RenderComponent.spriteName] of the object
     * and render the image onto the screen. The asset should be loaded in
     * [com.paperatus.swipe.Game.assets].
     *
     * The GameObject will be skipped if it does not contain a RenderComponent component
     *
     * Overriding this method allows for custom rendering.
     *
     * @param batch the SpriteBatch to render onto.
     */
    override fun render(batch: SpriteBatch) {
        nodeTraversal.traverse(nodeRenderer, root, batch)
    }

    /**
     * Adds a GameObject to the ObjectScene.
     *
     * @param gameObject the GameObject to add.
     */
    open fun addObject(gameObject: GameObject) = root.addChild(gameObject)

    /**
     * Removes the GameObject from the ObjectScene.
     *
     * @param gameObject the GameObject to remove.
     * @param identity true will use == to compare, false will use .equals().
     */
    open fun removeObject(gameObject: GameObject, identity: Boolean = true) =
            root.removeChild(gameObject, identity)

    //open fun queueRemove(gameObject: GameObject) {
        //removeQueue.add(gameObject)
    //}

    // TODO: Implement a map for each order for faster updates
//    private fun updateComponents(order: Component.Order) {
//        gameObjects.forEach { gameObject ->
//            gameObject.getComponents().values().forEach { component ->
//                if (component.order == order) component.update(componentDelta, gameObject)
//            }
//        }
//    }

    inline fun <T : Any> GdxArray<T>.operate(action: GdxArray<T>.() -> Unit) = action()
}
