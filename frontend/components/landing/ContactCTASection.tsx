import { motion, AnimatePresence } from 'framer-motion'
import { useFrame } from '@/context/FrameContext'
import { Button } from '@heroui/button'

export function ContactCTASection() {
  const { frame } = useFrame()

  const visible = frame >= 1020

  return (
    <AnimatePresence>
      <motion.section
        id="contact"
        className="relative z-10 px-6 py-24 sm:px-12 lg:px-32 bg-gradient-to-br from-violet-900/60 to-indigo-900/50 border-t border-white/10 shadow-2xl"
        initial={{ opacity: 0 }}
        animate={{ opacity: visible ? 1 : 0 }}
        exit={{ opacity: 0 }}
        transition={{ duration: 0.8, ease: 'easeInOut' }}
      >
        <div className="max-w-3xl mx-auto text-center text-white">
          <h2 className="text-4xl font-bold mb-4">¿Listo para comenzar?</h2>
          <p className="text-lg text-white/90 mb-8">
            Ponte en contacto con nosotros o regístrate para empezar a usar la plataforma.
            Estamos aquí para ayudarte a impulsar tu negocio.
          </p>

          <div className="flex flex-col sm:flex-row justify-center gap-4">
            <Button
              as="a"
              href="mailto:contact@dfcshop.site"
              color="secondary"
              variant="faded"
              size='lg'
              className="rounded-full"
            >
              Escríbenos
            </Button>
            <Button
              as="a"
              href="/auth"
              color="secondary"
              variant="solid"
              size='lg'
              className="rounded-full"
            >
              Crea tu cuenta
            </Button>
          </div>
        </div>
      </motion.section>
    </AnimatePresence>
  )
}
