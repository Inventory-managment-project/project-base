import { motion, AnimatePresence } from 'framer-motion'
import { useFrame } from '@/context/FrameContext'
import { Zap, BarChart3, ShieldCheck } from 'lucide-react'

export function FeaturesSection() {
  const { frame } = useFrame()

  const visible = frame >= 80 && frame <= 350

  const features = [
    {
      icon: <Zap size={32} />,
      title: 'Automatización',
      description: 'Optimiza procesos repetitivos y enfócate en lo que realmente importa.',
    },
    {
      icon: <BarChart3 size={32} />,
      title: 'Análisis en Tiempo Real',
      description: 'Visualiza métricas clave para tomar decisiones inteligentes.',
    },
    {
      icon: <ShieldCheck size={32} />,
      title: 'Seguridad Integrada',
      description: 'Tus datos protegidos con encriptación de grado empresarial.',
    },
  ]

  return (
    <AnimatePresence>
        <motion.section
          id="features"
          initial={{ opacity: 0 }}
          animate={{ opacity: visible ? 1 : 0 }}
          exit={{ opacity: 0 }}
          transition={{ duration: 0.8, ease: 'easeInOut' }}
          className="z-10 flex flex-col items-center justify-center gap-10 py-24 px-6 max-w-4xl mx-auto text-center"
        >
          <h2 className="text-4xl font-bold text-violet-700 py-4 px-6 bg-white/90 rounded-xl drop-shadow-lg">Características Clave</h2>
          <div className="grid md:grid-cols-3 gap-12 mt-10">
            {features.map((feature, index) => (
              <motion.div
                key={index}
                className="flex flex-col items-center gap-4 p-6 rounded-xl bg-white/90 shadow-lg border"
                initial={{ opacity: 0, y: 40 }}
                animate={{ opacity: visible ? 1 : 0.5, y: visible ? 0 : 20 }}
                transition={{ duration: 0.5, delay: index * 0.2 }}
              >
                <div className="text-violet-600">{feature.icon}</div>
                <h3 className="text-xl font-semibold">{feature.title}</h3>
                <p className="text-gray-700 text-sm">{feature.description}</p>
              </motion.div>
            ))}
          </div>
        </motion.section>
    </AnimatePresence>
  )
}
