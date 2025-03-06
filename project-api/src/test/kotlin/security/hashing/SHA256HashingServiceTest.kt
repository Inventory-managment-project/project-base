package mx.unam.fciencias.ids.eq1.security.hashing

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SHA256HashingServiceTest {

 private lateinit var hashingService: SHA256HashingService

 @BeforeEach
 fun setUp() {
  hashingService = SHA256HashingService()
 }

 @Test
 fun `verify salted hash returns true for correct value and hash`() {

  val value = "password"
  val saltedHash = SaltedHash(
   hash = "37c00033b5eac206b30a4514092f1d087a86643ccd1fad8bbdb237f01c74eab6",
   salt = "chop"
  )

  val result = hashingService.verifySaltedHash(value, saltedHash)

  // Then
  assertTrue(result)
 }

 @Test
 fun `verify salted hash returns false for incorrect value`() {
  // Given
  val wrongValue = "wrongPassword"
  val saltedHash = SaltedHash(
   hash = "37c00033b5eac206b30a4514092f1d087a86643ccd1fad8bbdb237f01c74eab6",
   salt = "chop"
  )

  // When
  val result = hashingService.verifySaltedHash(wrongValue, saltedHash)

  // Then
  assertFalse(result)
 }

 @Test
 fun `verify salted hash returns false for incorrect hash`() {
  // Given
  val value = "password123"
  val saltedHash = SaltedHash(
   hash = "incorrect5959abce4eda5f0e7a4e7ea53dce4fa0f0abbe8eaa63717e2fedincorrect",
   salt = "deadbeef"
  )

  // When
  val result = hashingService.verifySaltedHash(value, saltedHash)

  // Then
  assertFalse(result)
 }

 @Test
 fun `verify salted hash returns false for empty value`() {
  val emptyValue = ""
  val saltedHash = SaltedHash(
   hash = "5744323314533d2583cc8dd73ee7c98b45ce3c4c87c33c19d88bded2306d738d",
   salt = "chop"
  )

  val result = hashingService.verifySaltedHash(emptyValue, saltedHash)

  assertFalse(result)
 }
}