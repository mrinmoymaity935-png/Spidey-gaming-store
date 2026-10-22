package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.Product
import com.example.data.repository.AuthRepository
import com.example.data.repository.StoreRepository
import com.example.ui.components.QrCodeUtil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("SPIDEY", appName)
    }

    @Test
    fun `test indian phone number validation`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val authRepo = AuthRepository(context)

        // Valid Indian numbers
        assertNull(authRepo.validateIndianPhoneNumber("9876543210"))
        assertNull(authRepo.validateIndianPhoneNumber("+919876543210"))
        assertNull(authRepo.validateIndianPhoneNumber("7001234567"))

        // Invalid numbers
        assertNotNull(authRepo.validateIndianPhoneNumber("12345"))
        assertNotNull(authRepo.validateIndianPhoneNumber("5876543210")) // Doesn't start with 6-9
        assertNotNull(authRepo.validateIndianPhoneNumber("abcdefghij"))
    }

    @Test
    fun `test store repository cart calculations and order placement`() {
        val storeRepo = StoreRepository()
        val product = Product(
            id = "test_item",
            name = "Test Diamonds",
            category = "Diamonds",
            description = "Test description",
            priceInr = 100.0
        )

        storeRepo.addToCart(product)
        assertEquals(1, storeRepo.cart.value.size)
        assertEquals(100.0, storeRepo.cart.value.first().totalInr, 0.01)

        storeRepo.updateCartQuantity(product.id, 1)
        assertEquals(2, storeRepo.cart.value.first().quantity)
        assertEquals(200.0, storeRepo.cart.value.first().totalInr, 0.01)

        val order = storeRepo.placeOrder(
            userId = "usr_test",
            customerPhone = "+919876543210",
            customerName = "Tester",
            targetRecipient = "UID 12345"
        )

        assertNotNull(order.id)
        assertTrue(order.id.startsWith("SPD-"))
        assertEquals(200.0, order.totalAmountInr, 0.01)
        assertTrue(storeRepo.cart.value.isEmpty())
    }

    @Test
    fun `test upi qr code generation`() {
        val upiUri = "upi://pay?pa=spidey.gaming@okhdfcbank&pn=SPIDEY&am=100&cu=INR&tn=Order"
        val bitmap = QrCodeUtil.generateQrBitmap(upiUri, sizePx = 256)
        assertNotNull(bitmap)
        assertEquals(256, bitmap.width)
        assertEquals(256, bitmap.height)
    }

    @Test
    fun `test wishlist toggling and trending statuses`() {
        val storeRepo = StoreRepository()
        val testId = "prod_dia_100"
        val initialWishlist = storeRepo.wishlist.value.contains(testId)

        storeRepo.toggleWishlist(testId)
        assertEquals(!initialWishlist, storeRepo.wishlist.value.contains(testId))

        assertTrue(storeRepo.trendingStatuses.value.isNotEmpty())
    }
}
