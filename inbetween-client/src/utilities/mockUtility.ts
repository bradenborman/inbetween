export function simulateWait(time: number) {
  return new Promise(resolve => setTimeout(resolve, time));
}
