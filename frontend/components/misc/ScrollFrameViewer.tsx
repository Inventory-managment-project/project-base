import { useEffect, useRef, useState } from 'react'
import Lenis from 'lenis'
import { useFrame } from '@/context/FrameContext'

const TOTAL_FRAMES = 1121

export default function ScrollFrameViewer() {
  const { frame, setFrame } = useFrame();
  const containerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const lenis = new Lenis({ lerp: 0.2 });

    const raf = (time: number) => {
      lenis.raf(time);

      const scrollY = lenis.scroll;
      const maxScroll =
        (containerRef.current?.scrollHeight ?? 0) - window.innerHeight;

      const progress = Math.min(Math.max(scrollY / maxScroll, 0), 1);
      const frameIndex = Math.floor(progress * (TOTAL_FRAMES - 1));

      setFrame(frameIndex);

      requestAnimationFrame(raf);
    }

    requestAnimationFrame(raf);

    return () => {
      lenis.destroy()
    }
  }, [])

  const framePath = `/frames/frame_${String(frame + 1).padStart(4, '0')}.webp`

  return (
    <>
      <div ref={containerRef} className="absolute top-0 left-0 w-full h-[400vh]">
        <div
          className="fixed inset-0 z-0 pointer-events-none bg-black"
        >
          <img
            src={framePath}
            alt={`Frame ${frame}`}
            style={{
              width: '100%',
              height: '100%',
              objectFit: 'cover',
            }}
          />
        </div>
      </div>
    </>
  )
}
