import { useEffect } from "react";

export default function PrefetchImages() {
  useEffect(() => {
    for (let i = 1; i <= 1121; i++) {
      const src = `/frames/frame_${String(i).padStart(4, '0')}.webp`;
      const img = new Image();
      img.src = src;
    }
  }, []);

  return null;
}
