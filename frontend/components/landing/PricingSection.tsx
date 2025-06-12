import { motion, AnimatePresence } from 'framer-motion'
import { useFrame } from '@/context/FrameContext'
import { Button } from '@heroui/button'

const plans = [
  {
    name: 'Básico',
    price: '$0',
    description: 'Ideal para empezar',
    cta: 'Comienza gratis',
    features: ['5 usuario', 'Acceso limitado', 'Soporte por email'],
  },
  {
    name: 'Profesional',
    price: '$29/mes',
    description: 'Para negocios en crecimiento',
    cta: 'Suscribirse',
    features: ['10 usuarios', 'Inventario completo', 'Análisis de ventas'],
  },
  {
    name: 'Empresarial',
    price: '$99/mes',
    description: 'Todo lo que necesitas',
    cta: 'Contáctanos',
    features: ['Usuarios ilimitados', 'Integraciones avanzadas', 'Soporte dedicado'],
  },
]

export function PricingSection() {
  const { frame } = useFrame()

  const visible = frame >= 670 && frame <= 980

  return (
    <AnimatePresence>
      <motion.section
        id="pricing"
        className="relative z-10 py-20 px-6 lg:px-12"
        initial={{ opacity: 0 }}
        animate={{ opacity: visible ? 1 : 0 }}
        exit={{ opacity: 0 }}
        transition={{ duration: 0.6, ease: 'easeInOut' }}
      >
        <div className="max-w-6xl mx-auto text-center">
          <div className="py-4 px-6 bg-white/90 rounded-xl drop-shadow-lg gap-4 mb-12 max-w-[80%] mx-auto">
            <h2 className="text-4xl md:text-5xl font-bold">
              Planes para todos
            </h2>
            <p className="text-black/80 max-w-2xl mx-auto text-lg">
              Elige el plan que mejor se adapte a las necesidades de tu negocio.
            </p>
          </div>
          <div className="grid gap-8 md:grid-cols-3">
            {plans.map((plan, i) => (
              <motion.div
                key={plan.name}
                initial={{ opacity: 0, y: 30 }}
                animate={{ opacity: visible ? 1 : 0, y: visible ? 0 : 30 }}
                transition={{ delay: 0.1 * i }}
                className="bg-white/10 backdrop-blur-lg p-6 rounded-xl border border-white/20 text-black shadow-lg"
              >
                <h3 className="text-2xl font-bold mb-4">{plan.name}</h3>
                <p className="text-black/80 mb-6">{plan.description}</p>
                <div className="text-4xl font-bold mb-4">{plan.price}</div>
                <ul className="mb-6 space-y-2 text-sm text-black/80">
                  {plan.features.map((f, idx) => (
                    <li key={idx}>• {f}</li>
                  ))}
                </ul>
                <Button
                  color="secondary"
                  variant="solid"
                  size='lg'
                  className="w-full"
                >
                  {plan.cta}
                </Button>
              </motion.div>
            ))}
          </div>
        </div>
      </motion.section>
    </AnimatePresence>
  )
}
