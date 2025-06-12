'use client'

import { motion, AnimatePresence } from 'framer-motion'
import { useFrame } from '@/context/FrameContext'
import { Alsa, CeraVe, Vicio, Nothing, TresB, Aurrera } from '@/components/misc/clients'

const clients = [
  { name: 'Alsa', logo: Alsa },
  { name: 'CeraVe', logo: CeraVe },
  { name: 'Vicio.', logo: Vicio },
  { name: 'Nothing', logo: Nothing },
  { name: 'TresB', logo: TresB },
  { name: 'Aurrera', logo: Aurrera },
]

export function ClientsSection() {
  const { frame } = useFrame()

  const visible = frame >= 390 && frame <= 640

  return (
    <AnimatePresence>
        <motion.section
          id="clients"
          initial={{ opacity: 0 }}
          animate={{ opacity: visible ? 1 : 0 }}
          exit={{ opacity: 0 }}
          transition={{ duration: 0.8, ease: 'easeInOut' }}
          className="z-10 flex flex-col items-center justify-center gap-10 py-24 px-6 max-w-6xl mx-auto text-center"
        >
          <div className="bg-white/90 rounded-2xl shadow-xl p-10 border border-white/20 w-full">
            <h2 className="text-4xl font-bold text-violet-700 drop-shadow-md">Nuestros Clientes</h2>
            <p className="text-lg text-violet-500 max-w-2xl mx-auto mt-4">
              Empresas que confían en nosotros para llevar su gestión al siguiente nivel.
            </p>
            <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-8 items-center justify-center mt-12">
              {clients.map((client, index) => (
                <motion.div
                  key={client.name}
                  initial={{ opacity: 0, scale: 0.8 }}
                  animate={{ opacity: visible ? 1 : 0.4, scale: visible ? 1 : 0.9 }}
                  transition={{ delay: index * 0.1 }}
                  className="flex items-center justify-center p-4 rounded-lg bg-white/5 shadow-inner"
                >
                  <client.logo
                    className="w-24 h-24 md:w-32 md:h-32 object-contain"
                  />
                </motion.div>
              ))}
            </div>
          </div>
        </motion.section>
    </AnimatePresence>
  )
}
